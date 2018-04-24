package classes;

public class Fakultät {
	
	public static void main(String[] args) {
		
		
		System.out.println(check(5));
		
	}
	
	private static int check(int i) {
		int j = i;
		if(j<0) {
		return j;
		}
		else {
			return fak(j,j);
		}
		
	}
	
	private static int fak(int i,int j) {
		
		if (i>1) {
			j = j * (i-1);
			return fak(i - 1, j);
		}
		else {
			return j;
		}
	}

}
