package main;

import java.io.FileNotFoundException;
import java.util.Scanner;

import engine.QueryProcessor;

/**
 * Created by Jônatas on 04/12/2014.
 */
public class RunMKStream {
    private static String defaultDir = System.getProperty("user.dir") + "/src/xml";
//    private static String defaultDir = "D:\\data_queries";
    private static String[] lArgs = new String[9];
    private static String dir = System.getProperty("user.dir") + "/src/xml/";
    private static String queryFile = System.getProperty("user.dir") + "/src/xml/consulta.txt"; //FIXME: Aqui vai o arquivo .txt de consulta. O Algoritmo consulta todos os XMLs em dir.
    private static String processor = ""; // Nome do algoritmo
    private static String nodeType; //SLCA ou ELCA
    private static String numberOfQueries;
    private static String memoryTime = "";
    private static String numberOfStacks;
//    private static String pushingType = "allnodes_allstacks";
//    private static String pushingType = "minnodes_allstacks";
    private static String pushingType = "minnodes_groupstacks";



    public static void main(String ... args) {
        Scanner dataSet = new Scanner( System.in );
        String sDataSetType = "";
        String dataSetName = "";
        String queryTxt = "";
        int op = -1;
/*
        do{
            clearConsole();
            System.out.println("********* MKStream 2.0 ***********");
            System.out.println("Chose a Dataset:");
            System.out.println("1 - icde");
            System.out.println("2 - isfdb");
            System.out.println("3 - sigmod");
            System.out.println("4 - xmark");
            System.out.println("\n0 - QUIT\n");


            sDataSetType = dataSet.next();

            try {
                op = Integer.parseInt(sDataSetType);
            } catch (NumberFormatException e) {
                System.out.println("Only numbers, please.");
            }

            switch (sDataSetType) {
                case "0" : System.exit(0); break;
                case "1" : {
                    dataSetName = "ICDE";
                    queryTxt = menu(dataSetName);
                    if(queryTxt.equalsIgnoreCase("")) {
                        op = -1;
                    }
                    dir = defaultDir + "/datasets/icde/";
                    queryFile = defaultDir + "/queries/icde/" + queryTxt;
                } break;
                case "2" : {
                    dataSetName = "ISFDB";
                    queryTxt = menu(dataSetName);
                    if(queryTxt.equalsIgnoreCase("")) {
                        op = -1;
                    }
                    dir = defaultDir + "/datasets/isfdb/";
                    queryFile = defaultDir + "/queries/isfdb/" + queryTxt;
                } break;
                case "3" : {
                    dataSetName = "SIGMOD";
                    queryTxt = menu(dataSetName);
                    if(queryTxt.equalsIgnoreCase("")) {
                        op = -1;
                    }
                    dir = defaultDir + "/datasets/sigmod/";
                    queryFile = defaultDir + "/queries/sigmod/" + queryTxt;
                } break;
                case "4" : {
                    dataSetName = "XMARK";
                    queryTxt = menu(dataSetName);
                    if(queryTxt.equalsIgnoreCase("")) {
                        op = -1;
                    }
                    dir = defaultDir + "/datasets/xmark/";
                    queryFile = defaultDir + "/queries/xmark/" + queryTxt;
                } break;
                default:
                    System.out.println("Invalid Choice");
            }
        }while(op == -1);
        
        nodeType = menuNodeType();
        clearConsole();
        numberOfQueries = menuNumberOfQueries();
        clearConsole();
        numberOfStacks = menuNumberOfStacks();

        clearConsole();
        System.out.println("\n>> Running MKStream for Dataset [" + dataSetName + "] Query [" + queryTxt + "]\n\n");

        lArgs[0] = dir;
        lArgs[1] = queryFile;
        lArgs[2] = processor;
        lArgs[3] = nodeType;
        lArgs[4] = numberOfQueries;
        lArgs[5] = memoryTime;
        lArgs[6] = numberOfStacks;
        lArgs[7] = pushingType;

        
        */
        
        lArgs[0] = defaultDir + "/datasets/"+args[0].toLowerCase()+"/";
        if(args[3].equals("1"))
        	lArgs[1] = defaultDir + "/queries/" + "1st_experiment/type3_size4.txt";
        else if(args[3].equals("2"))
        	lArgs[1] = defaultDir + "/queries/"+args[0].toLowerCase()+"/" + "2nd_experiment/0l5kw_size5.txt";
        else if(args[3].equals("3"))
        	lArgs[1] = defaultDir + "/queries/"+args[0].toLowerCase()+"/" + "2nd_experiment/1l4kw_size5.txt";
        
        lArgs[2] = processor;
        lArgs[3] = args[0];
        lArgs[4] = args[1];
        lArgs[5] = memoryTime;
        lArgs[6] = args[2];
        lArgs[7] = pushingType;
        lArgs[8] = args[4];
        
        //System.out.println("dataset;#queries;#stacks;#experiment;#threads;");
        System.out.print(args[0]+";"+args[1]+";"+args[2]+";"+args[3]+";"+args[4]+";");
        
        try {
            QueryProcessor.run((lArgs));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String menu(String nameSubMenu) {
        String retorno = "";
        String next = "";
        int n = -1;
        Scanner scanner = new Scanner( System.in );
        do{
            clearConsole();
            System.out.println("\n\n>>>> " + nameSubMenu + " <<<<");
            System.out.println("1 - 1st experiment");
            System.out.println("2 - 2nd experiment");
            System.out.println("3 - 3rd experiment");
            System.out.println("\n0 - CANCEL\n");

            next = scanner.next();

            try {
                n = Integer.parseInt(next);
            } catch (NumberFormatException e) {
                System.out.println("Only numbers, please");
            }

        }while(n == -1);

        switch (n) {
            case 0 : break;
            case 1: retorno = "1st_experiment/type3_size4.txt"; break;
            case 2: retorno = subMenu2ndExperiment(); break;
            case 3: retorno = subMenu3thExperiment(); break;
            default:
                System.out.println("Invalid Choice");break;
        }

        return retorno;
    }


    private static String subMenu2ndExperiment() {
        String retorno = "";
        String next = "";
        Scanner scanner = new Scanner( System.in );
        int n = -1;
        do{
            clearConsole();
            System.out.println("\n\n>>>> 2nd Experiment <<<<");
            System.out.println("1 - 0l5kw_size5.txt");
            System.out.println("2 - 1l4kw_size5.txt");
            System.out.println("3 - 2l3kw_size5.txt");
            System.out.println("4 - 3l2kw_size5.txt");
            System.out.println("\n0 - CANCEL\n");

            next = scanner.next();
            try {
                n = Integer.parseInt(next);
            } catch (NumberFormatException e) {
                System.out.println("Only numbers, please");
            }
        }while(n == -1);

        switch (n) {
            case 0 : break;
            case 1 : retorno = "2nd_experiment/0l5kw_size5.txt"; break;
            case 2 : retorno = "2nd_experiment/1l4kw_size5.txt"; break;
            case 3 : retorno = "2nd_experiment/2l3kw_size5.txt"; break;
            case 4 : retorno = "2nd_experiment/3l2kw_size5.txt"; break;
            default:
                System.out.println("Invalid Choice"); break;
        }
        return retorno;
    }

    private static String subMenu3thExperiment() {
        String retorno = "";
        String next = "";
        Scanner scanner = new Scanner( System.in );
        int n = -1;
        do{
            clearConsole();
            System.out.println("\n\n>>>> 3rd Experiment <<<<");
            System.out.println("1 - type3_size2.txt");
            System.out.println("2 - type3_size4.txt");
            System.out.println("3 - type3_size6.txt");
            System.out.println("\n0 - CANCEL\n");

            next = scanner.next();
            try {
                n = Integer.parseInt(next);
            } catch (NumberFormatException e) {
                System.out.println("Only numbers, please");
            }
        }while(n == -1);

        switch (n) {
            case 0 : break;
            case 1 : retorno = "3rd_experiment/type3_size2.txt"; break;
            case 2 : retorno = "3rd_experiment/type3_size4.txt"; break;
            case 3 : retorno = "3rd_experiment/type3_size6.txt"; break;
            default:
                System.out.println("Invalid Choice"); break;
        }
        return retorno;
    }
    private final static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            }
            else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            //  Handle any exceptions.
        }
    }
    
	private static String menuNumberOfQueries() {
		String retorno = "";
		String next = "";
		Scanner scanner = new Scanner(System.in);
		int n = -1;
		do {
			clearConsole();
			System.out.println("\n\n>>>> NUMBER OF QUERIES <<<<");
			System.out.println("Insert the number of queries:\n");

			next = scanner.next();
			try {
				n = Integer.parseInt(next);
				retorno = next;
			} catch (NumberFormatException e) {
				System.out.println("Only numbers, please");
			}
		} while (n == -1);
		return retorno;
	}
	
	private static String menuNodeType() {
		String retorno = "";
		String next = "";
		Scanner scanner = new Scanner(System.in);
		int n = -1;
		do {
			clearConsole();
			System.out.println("\n\n>>>> NODE TYPE <<<<");
			System.out.println("1 - SLCA");
			System.out.println("2 - ELCA");

			next = scanner.next();
			try {
				n = Integer.parseInt(next);
			} catch (NumberFormatException e) {
				System.out.println("Only numbers, please");
			}
		} while (n == -1);

		switch (n) {
		case 1:
			retorno = "SLCA";
			break;
		case 2:
			retorno = "ELCA";
			break;
		default:
			System.out.println("Invalid Choice");
			break;
		}
		return retorno;
	}
	
	private static String menuNumberOfStacks() {
		String retorno = "";
		String next = "";
		Scanner scanner = new Scanner(System.in);
		int n = -1;
		do {
			clearConsole();
			System.out.println("\n\n>>>> NUMBER OF STACKS <<<<");
			System.out.println("Insert the number of queries:\n");

			next = scanner.next();
			try {
				n = Integer.parseInt(next);
				retorno = next;
			} catch (NumberFormatException e) {
				System.out.println("Only numbers, please");
			}
		} while (n == -1);
		return retorno;
	}
}
