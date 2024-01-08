package apresentacao;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import exceptions.*;
import negocio.Sistema;
import dados.*;

public class Main {


    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Sistema sistema = new Sistema();
        

        //System.out.println("Hello World");
        Scanner input = new Scanner(System.in);

        //para cadastro de bibliotecário:
        String nome_bibliotecario;
        int cod_assistente;

        //para cadastro de assistente:
        String nome_Assistente;

		//para cadastro de usuario:
		int opcao_categoria_usuario;
		String periodo = null;
		int periodo_do_usuario;

		//para cadastro de livro:
		int opcao_colecao_livro;
		String colecao = null;

		//para cadastro de exemplar;
		int cod_Livro = 0;

        //para emprestimo:
        int cod_usuario =0;
        int cod_Exemplar=0;
        
        //para reserva:
        int opcao_reserva = 0;
		//-----
        int opcao;
        do{
            menu();
            opcao = input.nextInt();
            System.out.print("\n");
            switch(opcao){
            case 1:
                System.out.println("*** Cadastrando Bibliotecário *** ");
                System.out.print("Digite o nome do bibliotecário: ");
                input.nextLine(); //para limpeza de buffer
                nome_bibliotecario = input.nextLine();

                System.out.println("Para os assistentes abaixo informe o id do assistente que esse bibliotecário irá supervisionar. \n");

                //Listar todos os assistentes:
                try {
                    List<Assistente> lista_de_Assistentes = sistema.listarTodoAssistentes();
                    for(Assistente dados_do_Assistente:lista_de_Assistentes) {
                        System.out.println(dados_do_Assistente);
                    }
                } catch (SelectException e) {
                    System.out.println("Erro ao listar todos os Assistentes");
                }

                //Pega o id(cod_a) do assistente e verifica se ele existe no banco de dados:
                try {
                    do {
                        System.out.println("\nDigite o ID do assistente do qual quer cadastrar: ");
                        cod_assistente = input.nextInt();
                        input.nextLine(); // Limpa o buffer do scanner
                    } while (sistema.Verifica_Se_Assistente_Existe(cod_assistente) != true);

                    Assistente assistente_dados = new Assistente(cod_assistente, "Nome Aux");

                    Bibliotecario bibliotecario_dados = new Bibliotecario(nome_bibliotecario,assistente_dados);

                    sistema.cadastrarBibliotecario(bibliotecario_dados);

                    System.out.println("Bibliotecário inserido com SUCESSO!");
                    


                } catch (SelectException e) {
                    System.out.println("Erro ao Listar Assistente na função.");
                } catch (InsertException e2) {
                    System.out.println("Erro ao inserir Bibliotecário no Banco de Dados");
                }

                try {
                    List<Bibliotecario> listar_bibliotecarios = sistema.listarTodosBibliotecarios();
                    System.out.print("------- Bibliotecarios -------\n");
                    System.out.print("| cod_b |      nome      | id_assistente |\n");
                    System.out.print("------------------------------\n");
                    for(Bibliotecario dados_Bibliotecario:listar_bibliotecarios) {
                        System.out.println(dados_Bibliotecario);
                    }
                } catch (SelectException e) {
                    System.out.println("Erro ao listar todos os Bibliotecarios");
                }
                
                break;
            case 2:
                System.out.println("*** Cadastrando Assistente *** ");
                System.out.print("Digite o nome do Assistente: ");
                input.nextLine();
                nome_Assistente = input.nextLine();

                Assistente assistente_dados = new Assistente(nome_Assistente);

                try {
                    sistema.cadastrarAssistente(assistente_dados);

                    System.out.println("\nAssistente inserido com SUCESSO! Lista de Assistentes:");
                    
                    List<Assistente> lista_de_Assistentes = sistema.listarTodoAssistentes();
                    for(Assistente dados_do_Assistente:lista_de_Assistentes) {
                        System.out.println(dados_do_Assistente);
                    }
                
                } catch (InsertException e) {
                    System.out.println("Erro ao inserir novo Assistente");
                } catch (SelectException e2) {
                    System.out.println("Erro ao listar todos os assistentes após inserir");
                }

                //ao inserir um novo assistente, perguntar qual bibliotecário irá supervisioná-lo. (retirar entao essa funcionalidade do caso 1.)
                //criar uma funcao "Supervisiona(id assistente)" e adicionar um ou mais bibliotecarios que irao supervisionar o assistente passado como parametro -> update na tabela bibliotecarios.
                break;
            case 3:
                System.out.println("*** Cadastrando Usuario *** ");
                //pegar dados do usuario a partir do teclado
				System.out.print("Digite o nome: ");
				input.nextLine(); //Limpa o buffer
				String nome = input.nextLine();

				System.out.print("Digite o endereço: ");
				String endereco = input.nextLine();

				System.out.print("Digite o telefone: ");
				String telefone = input.nextLine();

				do {
					System.out.println("Digite qual a categoria do usuario:");
					System.out.println("1. Aluno Graduação");
					System.out.println("2. Aluno Pós Graduação");
					System.out.println("3. Professor");
					System.out.println("4. Professor Pos Grad");
					opcao_categoria_usuario = input.nextInt();
					input.nextLine(); // Limpa o buffer do scanner
				} while (opcao_categoria_usuario < 1 || opcao_categoria_usuario > 4);

				Categoria categoria = Usuario.obterCategoria(opcao_categoria_usuario);

				if(categoria == Categoria.PROFESSOR || categoria == Categoria.PROFESSOR_POS_GRADUACAO) {
					
					do {
						System.out.println("Qual o periodo de trabalho do professor? ");
						System.out.println("1. Matutino/Vespertino");
						System.out.println("2. Noturno");
						System.out.println("3. Integral");
						periodo_do_usuario = input.nextInt();
						input.nextLine(); // Limpa o buffer do scanner

					} while (periodo_do_usuario < 1 || periodo_do_usuario > 3);

					switch (periodo_do_usuario) {
						case 1:
							periodo = "Matutino/Vespertino";
							break;
						case 2:
							periodo = "Noturno";
							break;
						case 3:
							periodo = "Integral";
							break;
						default:
							break;
					}
				} else if (categoria == Categoria.ALUNO_GRADUACAO) {
					periodo = "Matutino/Vespertino";
				} else if (categoria == Categoria.ALUNO_POS_GRADUACAO) {
					periodo = "Noturno";
				} else {
					System.out.println("Categoria inválida. Período definido como NULL.");
				}
			
				Usuario novo_usuario = new Usuario(nome, endereco, telefone, periodo,categoria);
				
				try {
                    sistema.cadastrarUsuario(novo_usuario);

                    System.out.println("USUARIO inserido com SUCESSO! Lista de Usuarios do Sistema:");

					lista_de_Usuarios_FUNC(sistema);
                    
                } catch (InsertException e) {
                    System.out.println("Erro ao inserir novo Usuário");
                } //catch (SelectException e2) {
                    //System.out.println("Erro ao Selecionar todos os Usuarios");
                //} 
				 
                //quando um usuario é criado, o id do assistente é nulo. O assistente só será utilizado durante a devolução do livro. e é o assistente que recoloca o livro na tabela de exemplares. -> fazer uma função para isso na parte "Devolução de Livro"    

                //
                break;
            case 4:
                System.out.println("*** Cadastrando Livro *** ");
				System.out.print("Digite o ISBN: ");
				input.nextLine(); //Limpa o buffer
				String ISBN = input.nextLine();
				
				System.out.print("Digite o título do livro: ");
				String titulo = input.nextLine();
				
				System.out.print("Digite o autor: ");
				String autor = input.nextLine();
				
				System.out.print("Digite a editora: ");
				String editora = input.nextLine();
				
				do {
					System.out.println("Digite qual a coleção do Livro:");
					System.out.println("1. Exatas");
					System.out.println("2. Humanas");
					System.out.println("3. Biológicas");
					System.out.println("4. Diversos");
					System.out.println("5. Reserva");
					opcao_colecao_livro = input.nextInt();
					input.nextLine(); // Limpa o buffer do scanner
				} while (opcao_colecao_livro < 1 || opcao_colecao_livro > 5);

				switch (opcao_colecao_livro) {
					case 1:
						colecao = "Exatas";
						break;
					case 2:
						colecao = "Humanas";
						break;
					case 3:
						colecao = "Biológicas";
						break;
					case 4:
						colecao = "Diversos";
						break;
					case 5:
						colecao = "Reserva";
						break;
					default:
						break;
				}
				Livro novo_livro = new Livro(ISBN, titulo, autor, editora, colecao);
				try {
					sistema.cadastrarLivro(novo_livro);
					
					System.out.println("Livro inserido com sucesso!\n");
			
				} catch (InsertException e) {
					System.out.println("Erro ao inserir novo livro.");
				}
				listarTodosOsLivrosFunc(sistema);
				
                break;
            case 5:
                System.out.println("*** Cadastrando Exemplar *** ");
				//listar os livros
				System.out.println("Selecione o id do livro do qual você quer cadastrar um exemplar: \n");
				listarTodosOsLivrosFunc(sistema);
				try {
					do {
						System.out.println("\nDigite o ID do Livro do qual quer cadastrar: ");
						cod_Livro = input.nextInt();
						input.nextLine(); // Limpa o buffer do scanner
					} while (sistema.Verifica_Se_Livro_Existe(cod_Livro) != true);
				} catch (SelectException e) {
					System.out.println("Erro ao tentar buscar ID do livro");
				}
				System.out.println("Digite a localização do exemplar:");
				String localizacao = input.nextLine();

				Livro livro_aux = new Livro(cod_Livro, null, null, null, null, null);
				Exemplar exemplar = new Exemplar(livro_aux, localizacao);
				try {
					sistema.cadastrarExemplarLivro(exemplar);
					System.out.println("Exemplar inserido com sucesso!");
					List<Exemplar> lista_de_Exemplares= sistema.ListarTodosOsExemplares();
                    for(Exemplar dados_do_Exemplar:lista_de_Exemplares) {
                        System.out.println(dados_do_Exemplar);
                    }
				} catch (InsertException e) {
					System.out.println("Erro ao cadastrar exemplar");
				} catch (SelectException e2) {
					System.out.println("Erro ao listar todos os exemplares");
				}
				
                break;
            case 6:
                System.out.println("*** Realizando Emprestimo de Exemplar *** ");
				//Digite o id do usuário que irá pegar o emprestimo.
                lista_de_Usuarios_FUNC(sistema);
                try {
                    do {
                        System.out.println("\nDigite o ID do usuário que irá pegar o livro emprestado: ");
                        cod_usuario = input.nextInt();
                        input.nextLine(); // Limpa o buffer do scanner
                    } while (sistema.Verifica_Se_Usuario_Existe(cod_usuario) != true);
                } catch (SelectException e) {
                    System.out.println("Erro ao procurar se usuario Existe");
                }
                listar_Todos_Exemplares(sistema);
                try {
                    do {
                        System.out.println("\nDigite o ID do Exemplar do qual você quer realizar um empréstimo");
                        cod_Exemplar = input.nextInt();
                        input.nextLine(); // Limpa o buffer do scanner

                        
                    } while (sistema.Verifica_Se_Exemplar_Existe(cod_Exemplar) != true);

                    //se o exemplar não existir na tabela de emprestimo, posso fazer o emprestimo direto
                    //porem se o exemplar existir na tabela de emprestimo,mas possui multas (sistema.esta_Disponivel_Exemplar) quer dizer que o usuario que realizou aquele emprestimo, já entregou o livro, em atraso, mas entregou.
                    if(sistema.Verifica_Se_Exemplar_Existe_Tabela_Emprestimo(cod_Exemplar) == false 
                        || sistema.esta_Disponivel_Exemplar(cod_Exemplar) == true ) {

                        //Realizar emprestimo.
                        Usuario usuario = new Usuario(cod_usuario);
                        Exemplar exemplar2 = new Exemplar(cod_Exemplar);
                        Emprestimo emprestimo = new Emprestimo(usuario, exemplar2);
                        System.out.println("Chegou aqui");
                        
                        if(sistema.cadastrar_Emprestimo(emprestimo) == 1) {
                            System.out.println("Emprestimo Realizado com Sucesso!!");
                        } else {
                            System.out.println("Emprestimo NÃO AUTORIZADO");
                        }
                            

                    } else {
                        //Se não cair no IF, quer dizer que o exemplar está emprestado.
                        System.out.println("O EXEMPLAR de código: ["+ cod_Exemplar +"] Já está emprestado.");
                        

                        do {
                            System.out.println("Deseja Reservar este exemplar?");
                            System.out.println("[0] NÃO");
                            System.out.println("[1] SIM");
                            opcao_reserva = input.nextInt();
                            input.nextLine(); // Limpa o buffer do scanner
    
                        } while (opcao_reserva < 0 || opcao_reserva > 1);
                        
                        if(opcao_reserva == 0) {
                            System.out.println("Reserva de Exemplar Cancelada");
                            break;
                        }
                        //Se opcao_reserva == 1, continuar o código reservando:
                        Usuario usuario = new Usuario(cod_usuario);
                        Exemplar exemplar2 = new Exemplar(cod_Exemplar);
                        Reserva reserva = new Reserva(usuario,exemplar2);
                        if(sistema.Reservar_Livro(reserva) == 1) {
                            System.out.println("Reserva Realizada com Sucesso!!");
                        } else {
                            System.out.println("Reserva NÃO AUTORIZADA");
                        }
                            

                    }
                
                } catch (SelectException e) {

                    System.out.println("Erro ao procurar se Exemplar está emprestado");
                    
                } 
                

				
                
                break;
            case 7:
                System.out.println("*** Localizar Usuario com o Exemplar de um livro *** ");
                //listar todos exemplares e verificar se o valor passado existe
                listar_Todos_Exemplares(sistema);
                int Id_exemplar2 =0;
                try {
                    do {
                        System.out.println("\nDigite o ID do Exemplar do qual você quer buscar qual usuario está com ele");
                        Id_exemplar2 = input.nextInt();
                        input.nextLine(); // Limpa o buffer do scanner
    
                        
                    } while (sistema.Verifica_Se_Exemplar_Existe(Id_exemplar2) != true);
                } catch (SelectException e) {
                    System.out.println("Erro ao verificar se exemplar existe");
                }
                
                String nome_usuario;
                try {
                    nome_usuario = sistema.Usuario_que_esta_com_Exemplar(Id_exemplar2);

                    if(nome_usuario == "ERRO" ) {
                        System.out.println("Não existe nenhum usuário com esse exemplar.");
                    } else {
                        System.out.println("Usuario que está com esse exemplar: " + nome_usuario);
                    }
                } catch (SelectException e) {
                    System.out.println("Erro ao buscar qual usuario esta com esse exemplar");
                }
                break;

            case 8:
                System.out.println("*** Listar todos os Emprestimos *** ");
                listar_Todos_emprestimosFunc(sistema);
                break;
            case 9:
                System.out.println("*** Listar todos os livros da Biblioteca *** ");
                listarTodosOsLivrosFunc(sistema);
            
                break;
            //case 10:
                //System.out.println("*** Renovar Emprestimo *** ");
                //lista todos os emprestimos por nome e nome do livro.
                //A renovação de emprestimo é um insert na tabela de emprestimo.
                //break;
            case 0:
                System.out.println("Encerrando...");
                break;
            
            default:
                System.out.println("Opção inválida.");
            }
        } while(opcao != 0);

        input.close();
        
    }


    public static void menu(){


        
        System.out.println("\n\tSistema de Biblioteca");
        System.out.println("0. Sair");
        System.out.println("1. Cadastrar Bibliotecário");
        System.out.println("2. Cadastrar Assistente");
        System.out.println("3. Cadastrar Usuario");
        System.out.println("4. Cadastrar Livro");
        System.out.println("5. Cadastrar Exemplar de um Livro");
        System.out.println("6. Realizar Empréstimo de um Exemplar");
        System.out.println("7. Localizar Usuário com um determinado Exemplar");
        System.out.println("8. Listar Todos os Emprestimos");
        System.out.println("9. Listar Todos os Livros");
		//System.out.println("10. Renovar Empréstimo");
        System.out.println("Opção:");
        
    }

	public static void listarTodosOsLivrosFunc(Sistema sistema) {
		try {
			List<Livro> filtroMesAno = sistema.ListarTodosOsLivros();
			for(Livro dados_do_livro:filtroMesAno) {
				System.out.println(dados_do_livro);
			}
		} catch (SelectException e) {
			System.out.println("Erro ao listar todos os livros");
		}

	}

    public static void lista_de_Usuarios_FUNC(Sistema sistema) {
		try {
			List<Usuario> lista_Usuarios = sistema.listar_Todos_Usuarios();
			for(Usuario dados_usuario:lista_Usuarios) {
				System.out.println(dados_usuario);
			}
		} catch (SelectException e) {
			System.out.println("Erro ao listar todos os Usuarios");
		}

	}

    public static void listar_Todos_Exemplares(Sistema sistema) {
		try {
			List<Exemplar> lista_Nome_Livro_exemplar = sistema.Listar_Nome_Livro_Exemplar();
			for(Exemplar dados_Exemplar:lista_Nome_Livro_exemplar) {
				System.out.println(dados_Exemplar);
			}
		} catch (SelectException e) {
			System.out.println("Erro ao listar todos os nomes dos exemplares");
		}

	}

    public static void listar_Todos_emprestimosFunc(Sistema sistema) {
        try {
			List<Emprestimo> lista_Emprestimos = sistema.listarTodos_Emprestimos();
			for(Emprestimo dados_emprestimo:lista_Emprestimos) {
				System.out.println(dados_emprestimo);
			}
		} catch (SelectException e) {
			System.out.println("Erro ao listar todos os Emprestimos");
		}
    }

}


