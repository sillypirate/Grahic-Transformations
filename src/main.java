import java.util.Random;
import java.util.Scanner;
import java.awt.Desktop;
import java.io.*;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
public class main {
	
	private static int nnum;
	private static double[][] ddatalines;
	private static int VVx0 = 0;
	private static int VVy0 = 0;
	private static int VVx1 = 640;
	private static int VVy1 = 480;

	static double[][] BasicTranslate(float Tx, float Ty){
		//double[][] T = {{1, 0, 0},{0, 1, 0}, {Tx, Ty, 1}};
		double[][] T = new double[3][3];
		T[0][0] = 1;
		T[0][1] = 0;
		T[0][2] = 0;
		T[1][0] = 0;
		T[1][1] = 1;
		T[1][2] = 0;
		T[2][0] = Tx;
		T[2][1] = Ty;
		T[2][2] = 1;
		return T;
	}
	
	static double[][] BasicScale(float Sx, float Sy){
		double[][] S = {{Sx, 0, 0},{0, Sy, 0}, {0, 0, 1}};
		return S;
		
	}
	
	static double[][] BasicRotate(float angle ){
		double[][] A = {{Math.cos(Math.toRadians(angle)), -Math.sin(Math.toRadians(angle)), 0},{Math.sin(Math.toRadians(angle)), Math.cos(Math.toRadians(angle)), 0}, {0, 0, 1}};
		return A;
	}
	
	
	static double[][] Concatenate(double[][] matrix1, double[][] matrix2){
		double[][] Matrix = new double[3][3];
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				for (int k = 0; k < 3; k++){
					Matrix[i][j] += matrix1[i][k] * matrix2[k][j];
				}
			}
		}
		matrix1 = Matrix;
		return matrix1;
	}
	
	static void ApplyTransformation(double[][] matrix, double[][] datalines, int num){
		double[][] temp = new double[nnum*2][3];
		for(int i = 0; i < nnum*2; i++){
			for(int j = 0; j < 3; j++){
				for(int k = 0; k < 3; k++){
					
						temp[i][j] += (datalines[i][k]*matrix[k][j]);// + (datalines[1][i] * matrix[j][1]) + (datalines[2][i] * matrix[j][2]);
					//System.out.println(temp);
					
				}
				
				
				//temp = 0;
			}
			
		}
		
		ddatalines = temp;
	}
	
	static void Viewport(int Vx0, int Vy0, int Vx1, int Vy1){
		VVx0 = Vx0;
		VVy0 = Vy0;
		VVx1 = Vx1;
		VVy1 = Vy1;
	}
	
	static void Displaypixels(double[][] datalines,  int num){
		try{
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.setTitle("Display Pixels");
			Display.create();
		}
		catch(LWJGLException e){
			e.printStackTrace();
		}
		 glMatrixMode(GL_PROJECTION);
		 glLoadIdentity();
		 
		 glOrtho(0, 640, 0, 480, 1, -1);
		 GL11.glViewport(VVx0,VVy0, VVx1,VVy1);
		 
		 
		 glMatrixMode(GL_MODELVIEW);
		 glLoadIdentity();
		 
		 int n = (num*2);
		 while(!Display.isCloseRequested()){
			 
			 for(int i = 0; i < n; i = i+2){
				lineAlg((int)datalines[i][0], (int)datalines[i][1], (int)datalines[i+1][0], (int)datalines[i+1][1]);
				
			 }
			 Display.sync(60);
			 Display.update();
		 }
		 Display.destroy();
	}
	
	static void plot(int x, int y){
		glBegin(GL_POINTS);
		glVertex2i(x,y);
	glEnd();
	}
	
static void lineAlg( int x0, int y0, int x1, int y1){
		
		
		
			int sx, sy;
			float dx = Math.abs(x1 - x0);
			float dy = Math.abs(y1 - y0);
			if (x0 < x1){
				sx = 1;
			}
			else{
				sx = -1;
			}
			if(y0 < y1){
				sy = 1;
			}
			else{
				sy = -1;
			}
			float err = dx - dy;

			
			while(true){//need work
				plot(x0, y0);
				if(x0 == x1 && y0 == y1){
					break;
				}
				float e2 = 2*err;
				if (e2 > -dy){
					err = err - dy;
					x0 = x0 + sx;
				}
				if(x0 == x1 && y0 == y1){
					plot(x0, y0);
					break;
				}
				if(e2 < dx){
					err = err + dx;
					y0 = y0 + sy;
				}
			}
			
		}
	
	static void Inputlines() throws FileNotFoundException{
		
	    Scanner reader = new Scanner(new FileInputStream("test1.txt"));
		nnum = reader.nextInt();
		ddatalines = new double[nnum*2][3];
		for(int i = 0; i < nnum*2; i++){
			for(int j = 0; j < 3; j++){
				ddatalines[i][j] = reader.nextInt();
			}
		}
	}
	
	static void Outputlines(double[][] datalines, int num, String filename) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter((filename), "UTF-8");
		writer.println(nnum);
		for (int i = 0; i < nnum*2; i++){
			for(int j = 0; j < 3; j++){
				writer.println(ddatalines[i][j]); 
		
			}
		}
		writer.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		
		boolean exit = false;
		int input;
		int n;
		Scanner keyboard = new Scanner(System.in);
		int vx0 = 0, vy0 = 0, vx1 = 640, vy1 = 480;
		float X;
		float Y, deg;
		double[][] trans = new double[3][3];
		trans[0][0] = 1;
		trans[0][1] = 0;
		trans[0][2] = 0;
		trans[1][0] = 0;
		trans[1][1] = 1;
		trans[1][2] = 0;
		trans[2][0] = 0;
		trans[2][1] = 0;
		trans[2][2] = 1;
		int change = 0;
		String fn;
		
		while (exit == false){
			System.out.print("Please select an option:\n" +
					"1: Input Lines\n" +
					"2: Set Vewport\n" +
					"3: Translate\n" +
					"4: Scale\n" +
					"5: Rotate\n" +
					"6: Apply Transformation\n" +
					"7: Display Lines\n" +
					"8: Output Lines\n" +
					"9: Line Generator\n" +
					"0: Exit\n");
			
			input = keyboard.nextInt();
			
			switch(input){
				case 1: 
					Inputlines();
					System.out.println("Done!");
					break;
					
				case 2:
					System.out.println("Enter Xmin");
					vx0 = keyboard.nextInt();
					System.out.println("Enter Ymin");
					vy0 = keyboard.nextInt();
					System.out.println("Enter Xmax");
					vx1 = keyboard.nextInt();
					System.out.println("Enter Ymax");
					vy1 = keyboard.nextInt();
					Viewport(vx0, vy0, vx1, vy1);
					System.out.println("Done!");
					break;
				
				case 3:
					System.out.println("Enter X");
					X = keyboard.nextFloat();
					System.out.println("Enter Y");
					Y = keyboard.nextFloat();
					if(change == 0){
						trans = BasicTranslate(X, Y);
						change++;
					}
					else{
						trans = Concatenate(trans, BasicTranslate(X,Y));
					}
					System.out.println("Done!");
					break;
				
				case 4:
					System.out.println("Enter X");
					X = keyboard.nextFloat();
					System.out.println("Enter Y");
					Y = keyboard.nextFloat();
					if(change == 0){
						trans = BasicScale(X, Y);
						change++;
					}
					else{
						trans = Concatenate(trans, BasicScale(X,Y));
					}
					System.out.println("Done!");
					break;
					
				case 5:
					System.out.println("Enther the Degrees");
					deg = keyboard.nextFloat();
					if(change == 0){
						trans = BasicRotate(deg);
						change++;
					}
					else{
						trans = Concatenate(trans, BasicRotate(deg));
					}
					System.out.println("Done!");
					break;
					
				case 6:
					ApplyTransformation(trans, ddatalines, nnum);
					System.out.println("Done!");
					trans[0][0] = 1;
					trans[0][1] = 0;
					trans[0][2] = 0;
					trans[1][0] = 0;
					trans[1][1] = 1;
					trans[1][2] = 0;
					trans[2][0] = 0;
					trans[2][1] = 0;
					trans[2][2] = 1;
					break;
					
				case 7:
					Displaypixels(ddatalines, nnum);
					break;
					
				case 8:
					System.out.println("Please enter the desired file name. Make sure to include \".txt\" in the file");
					fn = keyboard.next();
					Outputlines(ddatalines, nnum, fn);
					System.out.println("Done!");
					break;
					
				case 9:
					Random gen = new Random();
					System.out.println("How many lines do you want?");
					n = keyboard.nextInt();
					PrintWriter writer = new PrintWriter("test1.txt", "UTF-8");
					writer.println(n);
					while (n > 0){
						writer.println(gen.nextInt(641));
						writer.println(gen.nextInt(481));
						writer.println(1);
						writer.println(gen.nextInt(641));
						writer.println(gen.nextInt(481));
						writer.println(1);
						n--;
					}
					writer.close();
					System.out.println("Done!");
					break;
					
				case 0:
					exit = true;
					System.out.println("Good-bye");
					break;
					
				case 88:
					File file = new File ("c:");
					Desktop desktop = Desktop.getDesktop();
					desktop.open(file);
					System.out.println("Done!");
					break;
					
				case 99:
					for(int i = 0; i < 3; i++){
						for(int j = 0; j < 3 ; j++){
							System.out.println(trans[i][j]);
						}
					}
					break;
			}
			
		}

	}

}
