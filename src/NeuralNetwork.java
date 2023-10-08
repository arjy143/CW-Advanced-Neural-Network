import java.util.ArrayList;
import java.util.Random;
import java.io.File;  
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException; 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;  
import org.apache.poi.xssf.usermodel.XSSFSheet;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook;  


public class NeuralNetwork {
	
	//function that randomly generates weights and biases for a node
	public static float[] generateW(int inputs){
		float min = -1*(2/(float)inputs);
		float max = 2/(float)inputs;
		//System.out.println("min:"+min+", max:"+max);
		//use below code to get random numbers within interval
		float[] randomWeights = new float[inputs+1];
		for (int i = 0; i<=inputs; i++) {
			Random r = new Random();
			float random = min + r.nextFloat() * (max - min);
			randomWeights[i] = random;
		}
		return randomWeights;
	}
	
	public static float rootMeanSquare(ArrayList<Float> array){
		int n = array.size();
	    float square = 0;
	    float mean = 0;
	    float sqrt = 0;
	 
	    //calculates sum of squares of each item
	    for(Float item : array){
	        square += Math.pow(item, 2);
	    }	     
	    mean = (square / (float) n);
	    sqrt = (float)Math.sqrt(mean); 
	    return sqrt;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<Data> dataset = new ArrayList<Data>();
		
		File file = new File("C:\\Users\\laptop-a\\OneDrive - Loughborough University\\DataSet.xlsx");   //creating a new file instance  
		FileInputStream f = new FileInputStream(file);   //gets data from excel
		XSSFWorkbook wb = new XSSFWorkbook(f);   //create instance of workbook
		XSSFSheet sheet = wb.getSheetAt(0);     //identify specific sheet that data is taken from 
        DataFormatter dataFormatter = new DataFormatter();
        float Tmax = 28.9f;
        float Tmin = 7.2f;
        float Wmax = 1089.7f;
        float Wmin = 96.1f;
        float SRmax = 743.2f;
        float SRmin = 78.4f;
        float DSPmax = 102.8f;
        float DSPmin = 100.2f;
        float DRHmax = 95f;
        float DRHmin = 10f;
        float PanEmax = 1.28f;
        float PanEmin = 0.07f;
		for (Row row: sheet) {
			try {
				Data dataRow = new Data(0f,0f,0f, 0f,0f, 0f);
				//System.out.println(row.getCell(0));
				//gets contents of each cell in the row
				Cell Tcell = row.getCell(0);
				Cell Wcell = row.getCell(1);
				Cell SRcell = row.getCell(2);
				Cell DSPcell = row.getCell(3);
				Cell DRHcell = row.getCell(4);
				Cell PanEcell = row.getCell(5);
				//converts contents of each cell into a string
				String TcellValue = dataFormatter.formatCellValue(Tcell);
				String WcellValue = dataFormatter.formatCellValue(Wcell);
				String SRcellValue = dataFormatter.formatCellValue(SRcell);
				String DSPcellValue = dataFormatter.formatCellValue(DSPcell);
				String DRHcellValue = dataFormatter.formatCellValue(DRHcell);
				String PanEcellValue = dataFormatter.formatCellValue(PanEcell);
				//System.out.println(Tcell+" : "+Wcell);
				//converts contents of each string variable into a float
				float T = Float.parseFloat(TcellValue);
				float W = Float.parseFloat(WcellValue);
				float SR = Float.parseFloat(SRcellValue);
				float DSP = Float.parseFloat(DSPcellValue);
				float DRH = Float.parseFloat(DRHcellValue);
				float PanE = Float.parseFloat(PanEcellValue);
				//System.out.println(DRHcell+" : "+DRHcellValue+" : "+DRH);
				//standardises each variable
				T = (float) (0.8*((T-Tmin)/(Tmax-Tmin))+0.1);
				W = (float) (0.8*((W-Wmin)/(Wmax-Wmin))+0.1);
				SR = (float) (0.8*((SR-SRmin)/(SRmax-SRmin))+0.1);
				DSP = (float) (0.8*((DSP-DSPmin)/(DSPmax-DSPmin))+0.1);
				DRH = (float) (0.8*((DRH-DRHmin)/(DRHmax-DRHmin))+0.1);
				PanE = (float) (0.8*((PanE-PanEmin)/(PanEmax-PanEmin))+0.1);
				//put standardised variables inside array
				dataRow.setT(T);
				dataRow.setW(W);
				dataRow.setSR(SR);
				dataRow.setDSP(DSP);
				dataRow.setDRH(DRH);
				dataRow.setPanE(PanE);
				dataset.add(dataRow);
			}
			catch(NumberFormatException e) {
				//e.printStackTrace();
			}
//            for(Cell cell: row) {
//                String cellValue = dataFormatter.formatCellValue(cell);
//                float number = Float.parseFloat(cellValue);
//
//                System.out.print(cellValue + "\t");
//            }
            //System.out.println();
        }	
		//System.out.println(dataset.toString());
		 
		//1 hidden layer containing 2 nodes:
		
		//p = learning parameter
		float p = 0.1f;
		//creates a 2d array containing weights and biases of each node
		float[][] biases = new float[][]{NeuralNetwork.generateW(5),NeuralNetwork.generateW(5),NeuralNetwork.generateW(5),NeuralNetwork.generateW(5),NeuralNetwork.generateW(5),NeuralNetwork.generateW(5)};
		//print out random array generated
//		for (float[] thing : biases) {
//			System.out.println("array:");
//			for (float thing2 : thing) {
//				System.out.println("weight value:");
//				System.out.println(thing2);
//			}
//		}
		
		float[] activations = {0,0,0,0,0,0};
		float[] errors = {0,0,0,0,0,0};
		int epochCount = 1;
		//take first item in dataset
		ArrayList<Float> epochError = new ArrayList<Float>();
		//float[][] weightDifferences = new float[][]{{0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};

		while (epochCount<10000) {	
			ArrayList<Float> PanEerror = new ArrayList<Float>();
			//bold driver
			if (epochCount % 1000 == 0) {
				if (epochError.get(epochError.size()-1)>epochError.get(epochError.size()-2)) {
					p = p*0.7f;
				}else {
					p = p*1.3f;
				}
			}
			//annealing
//			p = (float) (p+(0.01-p)*(1-(1/(1+Math.exp(10-(20*epochCount/10000))))));	
//			System.out.println("p : "+p);
			//weight decay
			float upsilon = (float)(1/(p*epochCount));
			//float alpha = 0;
			for (Data d : dataset) {
				float T = d.getT();
				float W = d.getW();
				float SR = d.getSR();
				float DSP = d.getDSP();
				float DRH = d.getDRH();
				float PanE = d.getPanE();
				float[] inputs = {T,W,SR,DSP,DRH};
//				System.out.println(" T: "+T+" W: "+W+" SR: "+SR+" DSP: "+DSP+" DRH: "+DRH+" PanE: "+PanE);

				//do forward pass	
				//cycles through nodes and calculates activation value
				for (int i = 1; i<biases.length; i++) {
					float S = biases[i][0]+(biases[i][1]*T)+(biases[i][2]*W)+(biases[i][3]*SR)+(biases[i][4]*DSP)+(biases[i][5]*DRH);
					float U = (float) (1/(1+Math.exp(-1*S)));
					activations[i] = U;
//					System.out.println("S: "+S+", U: "+U);
				}
				//do output node
				float S = biases[0][0]+(biases[0][1]*activations[1])+(biases[0][2]*activations[2]+(biases[0][3]*activations[3])+(biases[0][4]*activations[4])+(biases[0][5]*activations[5]));
				float U = (float) (1/(1+Math.exp(-1*S)));
				activations[0] = U;
				//testing print
//				for (float value : activations) {
//					System.out.println("activation: "+value);
//				}

				//values calculated, now check accuracy
				//if result != PanE then we know more epochs are needed
//				System.out.println("generated value: "+String.format("%.4g%n", activations[0]));
//				System.out.println("correct value: "+String.format("%.4g%n", PanE));
//				String activationValue = String.format("%.4g%n", activations[0]);
//				float floatAc = Float.parseFloat(activationValue);
//				if (Float.parseFloat(String.format("%.2g%n", activations[0])) != Float.parseFloat(String.format("%.2g%n", PanE))) {
//					cleanEpoch = false;
//				}
				float errorValue = (float)Math.abs(PanE-activations[0])/(PanE);
				PanEerror.add(errorValue);
				//do backwards pass
				//error for output
				float omega = 0;
				int weightCount = 0;
				for (float[] array : biases) {
					for (float item : array) {
						weightCount += 1;
						omega += item * item;
					}
				}
				omega = (float)(omega/(2*weightCount));
				
				errors[0] = (float)(PanE-activations[0]+(upsilon*omega))*activations[0]*(1-activations[0]);
				//error for node 1
				errors[1] = (float)(biases[0][1]*errors[0]+(upsilon*omega))*activations[1]*(1-activations[1]);
				//error for node 2
				errors[2] = (float)(biases[0][2]*errors[0]+(upsilon*omega))*activations[2]*(1-activations[2]);
				errors[3] = (float)(biases[0][3]*errors[0]+(upsilon*omega))*activations[3]*(1-activations[3]);
				errors[4] = (float)(biases[0][4]*errors[0]+(upsilon*omega))*activations[4]*(1-activations[4]);
				errors[5] = (float)(biases[0][5]*errors[0]+(upsilon*omega))*activations[5]*(1-activations[5]);
//				errors[6] = (float)(biases[0][6]*errors[0]+(upsilon*omega))*activations[6]*(1-activations[6]);
//				errors[7] = (float)(biases[0][7]*errors[0]+(upsilon*omega))*activations[7]*(1-activations[7]);
//				errors[8] = (float)(biases[0][8]*errors[0]+(upsilon*omega))*activations[8]*(1-activations[8]);

				//testing print
//				for (float error : errors) {
//					System.out.println("error: "+error);
//				}
				//update weights and biases of output
				biases[0][0] = (float)biases[0][0]+(p*errors[0]*1);
				biases[0][1] = (float)biases[0][1]+(p*errors[0]*activations[1]);
				biases[0][2] = (float)biases[0][2]+(p*errors[0]*activations[2]);
				biases[0][3] = (float)biases[0][3]+(p*errors[0]*activations[3]);
				biases[0][4] = (float)biases[0][4]+(p*errors[0]*activations[4]);
				biases[0][5] = (float)biases[0][5]+(p*errors[0]*activations[5]);
//				biases[0][6] = (float)biases[0][6]+(p*errors[0]*activations[6]);
//				biases[0][7] = (float)biases[0][7]+(p*errors[0]*activations[7]);
//				biases[0][8] = (float)biases[0][8]+(p*errors[0]*activations[8]);

				//update weights and biases of hidden layer
				for (int i = 1; i<biases.length; i++) {
					biases[i][0] = biases[i][0]+(p*errors[i]*1);
					for (int j = 1; j<biases[i].length; j++) {
						//j-1 since inputs[0] corresponds to biases[i][1] and so forth
						biases[i][j] = biases[i][j]+(p*errors[i]*inputs[j-1]);			
					}
				}	
				
			}
//			for (Float thing : PanEerror) {
//				System.out.println(String.format("%.4g%n", thing));
//			}
			//System.out.println(epochCount);
			//for testing
			//complete = true;
			//if the results of all forward passes match the correct data, end the loop
			epochError.add(NeuralNetwork.rootMeanSquare(PanEerror));
			epochCount++;
			
		}

		//System.out.println("errors in each epoch:");
		
		XSSFSheet outputSheet = wb.createSheet("5nodesWD2");
		int rowCount = 0;
		for (Float error : epochError) {
			System.out.print(String.format("%.4g%n", error));
			Row row = outputSheet.createRow(++rowCount);
			Cell cell = row.createCell(0);
			cell.setCellValue((float) error);
		}
		System.out.println(p);
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
            wb.write(outputStream);
        }
//		System.out.println(epochError.get(0));
//
//		System.out.println(epochError.get(epochError.size() - 1));
	}
}	




