module mfo_trab {
    
  type Tipo_Criatura = Mago | Cacador | Druida | Monstro | Ilusao //Ilusao -> Ovelha
  type Option[a] = Some(a) | None

  type Criatura = {
      hp: int,
      iniciativa: int, //indica a ordem de atuacao na batalha 
      esta_paralisado: bool,
      esta_cego: bool,
      eh_urso: bool,
      pontos_de_vida_original: int, //armazenar os pontos de vida originais antes de transformações (urso)
      //^^ existe alguma outra forma de fazer isso? 
      tipo_Criatura: Tipo_Criatura
  }

  pure def max(x: int, y: int): int =
    if (x > y) x else y

  pure def damage(criatura: Criatura, dano: int): Criatura = {
     { ...criatura, hp: max(0, criatura.hp - dano) }
  }

  pure def paralyze(criatura: Criatura): Criatura = {
    { ...criatura, esta_paralisado: true}
  }

  pure def un_paralyze(criatura: Criatura): Criatura = {
    { ...criatura, esta_paralisado: false}
  }

  pure def blind(criatura: Criatura): Criatura = {
    { ...criatura, esta_cego: true}
  }

  //altera o hp para 60 e armazena os pontos de vida originais para quando voltar ao normal
  pure def transform_to_bear(criatura: Criatura): Criatura = {
    { ...criatura, hp: 60, eh_urso: true, pontos_de_vida_original: criatura.hp}
  }

  pure def revert_from_bear(criatura: Criatura): Criatura = {
    { ...criatura, eh_urso: false, hp: criatura.pontos_de_vida_original}
  }

  // Função para criar uma nova ilusão (ovelha)
  

  
  

  var order: List[Criatura]
  var round: int
  var start: bool

  type Effect = Unparalyzed | Paralyzed | Blind | BearForm | Ilussion
  type TurnDescription = 
  | Attack({attacker: Criatura, receiver: Criatura, damage: int})
  | Buff({caster: Criatura, receiver: Criatura, effect: Effect})
  | Imunity({caster: Criatura})
  | IsParalyzed({creature: Criatura})

  var last_attack: Option[List[TurnDescription]]





  //===================================ACTIONS========================//

  action attack(attacker: Criatura, target: Criatura): bool = {


    val dano = if (attacker.tipo_Criatura == Monstro and round == 0) 10 else 20
    val dano_ataque = if (attacker.tipo_Criatura != Monstro) 10 else dano

    // Selecionar o alvo prioritário se o atacante for um monstro
    val real_target = order.foldl(target, (acc, criatura) => 
    if (acc == target and (criatura.tipo_Criatura == Ilusao or criatura.eh_urso) and criatura.hp > 0) {
        criatura // Alvo prioritário encontrado, acumula como resultado
    } else {
        acc // Mantém o acumulador como está
    }
)


    all {
        // Verificar se o atacante e o alvo estão em condições de atacar e receber o ataque
        real_target.hp > 0,

        // Aplicar o dano ao alvo
        order' = order.foldl([], (acc, criatura) =>
            if (criatura == real_target) {
                acc.concat([damage(criatura, dano_ataque)])
            } else {
                acc.concat([criatura])
            }
        ),
        
        // Atualizar o estado do último ataque realizado
        last_attack' = Some([Attack({attacker: attacker, receiver: real_target, damage: dano_ataque})])
    }
}


  /*
  action attack(attacker: Criatura, target: Criatura): bool = {

      val dano = if (attacker.tipo_Criatura == Monstro and round == 0) 10 else 20
      val dano_ataque = if (attacker.tipo_Criatura != Monstro) 10 else dano
    
    

    all {
        // Verificar se o atacante e o alvo estão em condições de atacar e receber o ataque
        //not attacker.esta_paralisado,
        target.hp > 0,

        // Aplicar o dano ao alvo
        order' = order.foldl([], (acc, criatura) =>
            if (criatura == target) {
                acc.concat([damage(criatura, dano_ataque)])
            } else {
                acc.concat([criatura])
            }
        ),
        
        // Atualizar o estado do último ataque realizado
        last_attack' = Some([Attack({attacker: attacker, receiver: target, damage: dano_ataque})])
    }
  
  }

  */
  action paralyze_attack(attacker: Criatura, receiver: Criatura, nxt: List[Criatura]): bool = {
    all {
        // Verificar se estão em equipes diferentes e o atacante não está paralisado ou provocand
        attacker.esta_paralisado == false,
        
        // Apenas monstros podem aplicar paralisia
        attacker.tipo_Criatura == Monstro,

        // Ambos devem estar vivos
        attacker.hp > 0,
        receiver.hp > 0,

        // Aplicar a paralisia ao alvo
        order' = nxt.foldl([], (acc, i) => acc.append(
            if (i == receiver) paralyze(i) else i
        )),

        // Atualizar o último ataque com o efeito de paralisia
       last_attack' = Some([Buff({caster: attacker, receiver: receiver, effect: Paralyzed})])

    }
}


  action unparalyze_attack(attacker: Criatura, receiver: Criatura, nxt: List[Criatura]): bool = {
    all {
        // Verificar que o atacante é diferente do alvo e que ambos são personagens
        attacker != receiver,
        attacker.tipo_Criatura != Monstro,
        receiver.tipo_Criatura != Monstro,

        // Verificar se ambos estão vivos e aptos para a ação
        attacker.hp > 0,
        receiver.hp > 0,
        attacker.esta_paralisado == false,
        receiver.esta_paralisado == true,

        // Atualizar a lista order' desparalisando o receiver
        order' = nxt.foldl([], (acc, i) => acc.append(
            if (i == receiver) un_paralyze(i) else i
        )),

        // Atualizar last_attack com o efeito de desparalisia
        last_attack' = Some([Buff({ 
            caster: attacker, 
            receiver: receiver, 
            effect: Unparalyzed
        })])
    }
}


  action blind_attack(attacker: Criatura, receiver: Criatura, nxt: List[Criatura]): bool = {
    all {
        // Condições para que a cegueira seja aplicada
        attacker.tipo_Criatura == Cacador,  // O atacante deve ser um Caçador
        receiver.tipo_Criatura == Monstro, // O alvo deve ser um Monstro
        attacker.esta_paralisado == false,    // O atacante não pode estar paralisado
        attacker.hp > 0,                   // O atacante precisa estar vivo
        receiver.hp > 0,                   // O alvo também precisa estar vivo

        // Aplicar o efeito de cegueira ao Monstro
        order' = nxt.foldl([], (acc, i) => acc.append(
            if (i == receiver) blind(i) else i
        )),

        // Atualizar o último ataque para registrar o efeito de cegueira
        last_attack' = Some([Buff({caster: attacker, receiver: receiver, effect: Blind})])
    }
}

  action transform_to_bear_action(attacker: Criatura, nxt: List[Criatura]): bool = {
      all {
          // Verificar que o atacante é um Druida e está em condições de agir
          attacker.tipo_Criatura == Druida,
          attacker.esta_paralisado == false,
          attacker.hp > 0,

          // Aplicar a transformação em urso
          order' = nxt.foldl([], (acc, i) => acc.append(
              if (i == attacker) transform_to_bear(i) else i
          )),

          // Atualizar o último ataque para registrar a transformação
          last_attack' = Some([Buff({caster: attacker, receiver: attacker, effect: BearForm})])
      }
  }

  action create_illusion(attacker: Criatura, nxt: List[Criatura]): bool = {
    // Cria uma instância da ilusão usando a função pura
    val illusion = {
          hp: 1,
          iniciativa: 0, // Iniciativa baixa para que a ilusão não tenha um turno próprio
          esta_paralisado: false,
          esta_cego: false,
          eh_urso: false,
          pontos_de_vida_original: 1,
          tipo_Criatura: Ilusao // Tipo especial para identificar a ilusão
      }

    all {
        // Verificar que o atacante é um Mago e está em condições de agir
        attacker.tipo_Criatura == Mago,
        attacker.esta_paralisado == false,
        attacker.hp > 0,

        // Adicionar a ilusão à lista de criaturas `order`
        order' = nxt.concat([illusion]),

        // Atualizar o último ataque para registrar a criação da ilusão
        last_attack' = Some([Buff({caster: attacker, receiver: illusion, effect: Ilussion})])
    }
}



  pure def sort(l: List[Criatura]): List[Criatura] = {
      pure val list_d20 = range(1, 21)
      list_d20.foldl([], (acc, i) => acc.concat(l.select(x => x.iniciativa == i)))
    }

  action init = {

    pure val d20 = 1.to(20) 
    nondet iniciativa_mago = d20.oneOf()
    nondet iniciativa_cacador = d20.oneOf()
    nondet iniciativa_druida = d20.oneOf()
    nondet iniciativa_monstro = d20.oneOf()

    all {
      order' = sort([
        //Mago
        {hp:20,iniciativa:iniciativa_mago,esta_paralisado:false,esta_cego:false,eh_urso:false,pontos_de_vida_original:20,tipo_Criatura:Mago},
        //Caçador
        {hp:20,iniciativa:iniciativa_cacador,esta_paralisado:false,esta_cego:false,eh_urso:false,pontos_de_vida_original:20,tipo_Criatura:Cacador},
        //Druida
        {hp:20,iniciativa:iniciativa_druida,esta_paralisado:false,esta_cego:false,eh_urso:false,pontos_de_vida_original:20,tipo_Criatura:Druida},
        //Monstro 1
        {hp:100,iniciativa:iniciativa_monstro,esta_paralisado:false,esta_cego:false,eh_urso:false,pontos_de_vida_original:100,tipo_Criatura:Monstro},

        //Monstro 2
        {hp:100,iniciativa:iniciativa_monstro,esta_paralisado:false,esta_cego:false,eh_urso:false,pontos_de_vida_original:100,tipo_Criatura:Monstro},

      ]),
        
        round' = 0,
        last_attack' = None
    
    } 
  }

  pure def different_team(c1: Criatura, c2: Criatura): bool = {
    or {
      c1.tipo_Criatura == Monstro and c2.tipo_Criatura != Monstro,
      c1.tipo_Criatura != Monstro and c2.tipo_Criatura == Monstro,
    }
  }


  
  action step = all {
    // Seleciona a criatura atual com base no índice `round` na lista `order`
    val attacker = order[round]

    // Filtrar alvos vivos, distinguindo aliados e inimigos
    val everyone = order.indices().map(i => order[i])
    val enemies = everyone.filter(x => different_team(attacker, x) and x.hp > 0)
    val allies = everyone.filter(x => not(different_team(attacker, x)) and x.hp > 0)

    // Selecionar alvos aleatórios
    nondet enemy = enemies.oneOf()
    nondet ally = allies.oneOf()

    // Define o estado atual dos buffs para limpar quaisquer efeitos temporários, se aplicável
    val now_order = order.foldl([], (acc, i) =>
        if (i.eh_urso and i == attacker) acc.concat([revert_from_bear(i)])
        else if (i.esta_cego and i == attacker and attacker.tipo_Criatura == Monstro) acc.concat([{ ...i, esta_cego: false }])
        else acc.concat([i])
    )

    // Executa a ação apropriada com base no tipo de criatura com uma estratégia defensiva
    any {
        if (attacker.tipo_Criatura == Monstro) {
            // Monstros atacam as ilusões ou o urso se disponíveis; caso contrário, atacam um personagem
            val real_target = order.foldl(enemy, (acc, criatura) => 
                if ((criatura.tipo_Criatura == Ilusao or criatura.eh_urso) and criatura.hp > 0) criatura else acc
            )
            any {
                attack(attacker, real_target),
                paralyze_attack(attacker, real_target, now_order)
            }
        } 
        else if (allies.exists(ally => ally.esta_paralisado)) {
            // Prioridade para desparalisar aliados se houver algum paralisado
            unparalyze_attack(attacker, ally, now_order)
        } 
        else {

          any {
            if (enemy.esta_paralisado or enemy.hp <= 0) {
                attack(attacker, enemy)
            } else {
                unparalyze_attack(attacker, enemy, now_order)
            }
          }
        } 
        
    },

    // Atualizar o índice de `round` para o próximo turno
    round' = (round + 1) % order.length()
}






 



  val no_deaths = order.foldl(true, (acc, i) => acc and (i.tipo_Criatura == Monstro or i.hp > 0))


  

}