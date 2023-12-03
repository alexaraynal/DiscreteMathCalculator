import java.util.*; //I/O
import java.awt.*;
import java.awt.event.*;

class Set<T> //T represents type
{
	ArrayList<T> data;

	public Set()
	{
		data = new ArrayList<>();	
	}

	public void add(T element)
	{
		if(data.contains(element) == false)
		{
			data.add(element);
		}
	}

	public boolean contains(T element)
	{
		return data.contains(element);
	}

	public boolean remove(T element) //checks if the element is in the set and removes it
	{
		if(data.contains(element) == true)
		{
			data.remove(element);
			return true;
		}
		else return false;
	}

	public int search(T element) //Searches for element and returns position
	{
		return(data.indexOf(element));
	}

	public T get(int pos) //Searches for element and returns position
	{
		return(data.get(pos));
	}


	public String toString() //Gives format to string conversion
	{
		int i;
		String s = "[";
		for(i=0; i<(data.size()); i++)
		{
			s = s + data.get(i);
			if(i<(data.size() - 1))
			{
				s = s + ", ";
			}
		}
		s = s + "]";
		return(s);
	}

	public boolean equals(Set<T> b)
	{
		return(this.data.equals(b.data)); //Compares both arraylists
	}


	//Union AUB --> A.Union(B)
	public Set<T> union(Set<T> b)
	{
		Set<T> u = new Set<T>();
		int i;

		//Copy all elements from A to union

		for(i = 0; i<(this.data.size()); i++)
		{
			u.add(this.data.get(i));
		}

		//We add all B elements that differ
		for(i=0; i<(b.data.size()); i++)
        {
            
            if( (this.search(b.data.get(i))) == -1)
            {
                u.add(b.data.get(i)); 
            }
        }

        return u;
	}

	//Intersection
	public Set<T> intersect(Set<T> b)
	{
		Set<T> inter = new Set<T>();
		int i;

		//Search all elements from one set on the other

		for(i=0; i<(this.data.size()); i++)
        {
            if((this.search(b.data.get(i))) != -1)
            {
                inter.add(this.data.get(i)); //Si el elemento esta en el conjunto, lo agregamos al arreglo
            } 
        }
        return(inter);
	}

	//Cartesian Product
	public Set<OrderedPair<T>> cartprod(Set<T> b)
	{
		Set<OrderedPair<T>> cp = new Set<OrderedPair<T>>();
		int i,j;

		//We associate each A element with all B elements
		for(i=0; i<this.data.size(); i++)
        {
            for(j=0; j<b.data.size(); j++)
            {
                OrderedPair<T> p = new OrderedPair<>(this.data.get(i), b.data.get(j));
                cp.add(p);
            }
        }
        return(cp);
	}

	//Difference
	public Set<T> difference(Set<T> b)
	{
		Set<T> dif = new Set<T>();
		int i;

		// Copy all elements from A to dif
		for (i = 0; i < this.data.size(); i++) 
		{
    		dif.add(this.data.get(i));
		}

		// Delete all elements from dif that are in b
		i = 0;
		while (i < dif.data.size()) 
		{
    		if (b.data.contains(dif.data.get(i))) 
    		{
        		dif.data.remove(i); // Remove the element at index i in dif
    		} 
    		else 
    		{
        		i++; // Increment the index if no removal is performed
   			}
		}


		return(dif);
	}


}


class Relation<T> extends Set<OrderedPair<T>>
{

	Set<T> a;
	Set<T> b;
	BoolMatrix mr;

	public Relation(Set<T> a, Set<T> b)
	{
		data = new ArrayList<OrderedPair<T>>();	
		this.a = a;
		this.b = b;
		this.relmatrix();
	}


	//Function or not
	public boolean functioncheck()
	{
		//Check cardinality
		if(this.data.size() == a.data.size())
		{
			HashSet<T> uniqueElements = new HashSet<>();
			for(int i = 0; i<(this.data.size()); i++)
			{
				if (!uniqueElements.add(this.data.get(i).getOne())) 
				{
            		// If add() returns false, it means the element is a duplicate
            		return false;
        		}
			}
			return true;
		}
		return(false);
	}


	//Injective or not	
	public boolean isInject()
	{
		boolean flag = false;
		HashSet<T> set = new HashSet<>();
		for(int i=0; i<(this.data.size()); i++)
		{
			if (!set.add(this.data.get(i).getTwo())) 
			{
            	// If add() returns false, it means the element is a duplicate
            	return false;
        	}
        	else flag = true;
		}
		return flag;
	}

	//Surjective or not
	public boolean isSurject()
	{
		ArrayList<T> codomainElements = new ArrayList<T>();
		codomainElements.addAll(b.data);

		for(int i=0; i<(this.data.size()); i++)
		{
			codomainElements.remove(this.data.get(i).getTwo());
		}

		return codomainElements.isEmpty();
	}

	//Bijective or not
	public boolean isBiject()
	{
		return(this.isInject()&&this.isSurject());
	}


	//Create Relation Matrix

	public void relmatrix()
	{
		int i;
		T one, two;
		mr = new BoolMatrix(a.data.size(), b.data.size());

		//Initialize our matrix with 0s

		mr.setMatrix0();

		//Search all elements of R and save the indexes (a,b)
		for(i=0; i< this.data.size(); i++)
		{
			one = this.data.get(i).getOne();
			two = this.data.get(i).getTwo();

			this.mr.setElement(a.search(one), b.search(two), 1); //One represents a true value, the relation between one and two exists
		}

	}


	//Reflexive
	
	public boolean isReflexive()
	{
		int i;
		boolean flag = false;

		if(this.a.equals(this.b))
		{
			for(i=0; i<mr.getRow(); i++)
			{
				if(mr.getElement(i,i) == 1)
				{
					flag = true;
				}
				else return false;
			}
			return flag;	
		}
		else 
		{
			System.out.println("This property only applies to binary relationships");
			return false;
		}
		
	}


	//Transitive
	//M*M <= M
	public boolean isTransitive()
	{
		
		if(this.a.equals(this.b))
		{
			return (mr.product(mr).minorEqual(mr)); //M^2 is less or equals to M
		}
		else 
		{
			System.out.println("This property only applies to binary relationships");
			return false;
		}
		
	}


	//Symmetric
	//M^t = M
	public boolean isSymmetric()
	{
		

		if(this.a.equals(this.b))
		{
			return (mr.transpose()).equals(mr);	
		}
		else 
		{
			System.out.println("This property only applies to binary relationships");
			return false;
		}
			
	}

	//Antisymmetric
	//MintM^t <= I
	public boolean isAntisymmetric()
	{
		int i,j, cont1=0, cont2=0;
		
		
		if(this.a.equals(this.b))
		{
			for(i=0; i<mr.getRow(); i++ )
			{
				for(j=0; j<mr.getCol(); j++)
				{
					if(i!=j)
					{
						cont1++;
						if((mr.getElement(i,j) == 0)||(mr.getElement(j,i)==0))
						{
							cont2++;
						}
					}
				}
			}
			return(cont1 == cont2);	
		}
		else 
		{
			System.out.println("This property only applies to binary relationships");
			return false;
		}
			
	}

	
}

class PartialOrderR<T> extends Relation<T>
{

    public PartialOrderR(Set<T> a, Set<T> b) 
    {
        super(a, b);
    }

    // Additional method to check for partial order
    // Allows to create a Patrial Order Relation from a Relation that satisfies the conditions
   public PartialOrderR(Relation<T> relation) 
   {
        super(relation.a, relation.b);
        if (relation.isReflexive() && relation.isAntisymmetric() && relation.isTransitive()) 
        {
            this.data = relation.data;
            this.mr = relation.mr;
        } 
        else 
        {
            System.out.println("The given relation is not a partial order.");
        }
    }

    public ArrayList<T> getIsoVert()
    {
    	ArrayList<T> isovertex = new ArrayList<T>();

    	//i gives us the vertex we are analyzing
    	 for (int i = 0; i < this.mr.getRow(); i++) 
    	 {
	        boolean flag = true;

	        for (int j = 0; j < this.mr.getCol(); j++) 
	        {
	            if (i != j && (mr.getElement(i, j) != 0 || mr.getElement(j, i) != 0)) 
	            {
	                flag = false;
	                break;
	            }
	        }

	        if (flag) 
	        {
	            isovertex.add(this.a.get(i));
	        }
	    }



    	return isovertex;
    }

    public ArrayList<ArrayList<Object>> getHass() 
    {
	    ArrayList<OrderedPair<Object>> orderHass = new ArrayList<>();
	    
	    //We create an array list of ordered pairs in which we assign a "weight" to each vertex
	    for (int i = 0; i < this.mr.getRow(); i++) 
	    {
	        int cont = 0;
	        for (int j = 0; j < this.mr.getCol(); j++) 
	        {
	            if (mr.getElement(j,i) == 1) 
	            {
	                cont++;  //We determine the weight by counting the number of one's in each row
	            }
	        }
	        orderHass.add(new OrderedPair<>(this.a.get(i), cont));
	    }
	    //We sort our array by weight (second component of our ordered pair)
	    orderHass.sort(Comparator.comparingInt(pair -> (Integer) pair.getTwo()));

	    //We now create a new array of arrays where we store group of components that share the same weight/level
	    ArrayList<ArrayList<Object>> weightedHass = new ArrayList<>();
	    ArrayList<Object> group = new ArrayList<>();
	    
	    for (int i = 0; i < orderHass.size(); i++) 
	    {
	        if (i > 0 && !orderHass.get(i).getTwo().equals(orderHass.get(i - 1).getTwo())) 
	        {
	            weightedHass.add(new ArrayList<>(group));
	            group.clear();
	        }
	        group.add(orderHass.get(i).getOne());
	    }

	    if (!group.isEmpty()) 
	    {
	        weightedHass.add(new ArrayList<>(group));
	    }

	    return weightedHass;
	}


    public void paintHass(Graphics g, int l, int w)
    {
    	//Set Color for the points
    	g.setColor(Color.BLACK);
    	// Set the color for the text
        g.setColor(Color.BLACK);
        // Set the font for the text
        g.setFont(new Font("Arial", Font.BOLD, 12));
    	int diameter = 10; // Diameter of each point

    	ArrayList<ArrayList<Object>> orderHass = this.getHass();

		int lvlLen = l / (orderHass.size()); // Divides the length of the frame between the number of levels (size of the array)
		int ylen = 0;

		for (int i = orderHass.size() -1; i >= 0; i--) 
		{
		    // Each iteration represents a level
		    int lvlW = w / orderHass.get(i).size();
		    int xwidth = 0;

		    // Draw each point on each level
		    for (int j = 0; j < orderHass.get(i).size(); j++) 
		    {
		        int x = xwidth + (lvlW / 2); // X-coordinate of the center
		        int y = ylen + (lvlLen / 2); // Y-coordinate of the center
		        g.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);
		        String text = (String) orderHass.get(i).get(j);
		        System.out.println("Painted:"+text);

		        if (i > 0) 
		        	connectPoints(g, orderHass, i, j, x, y, lvlLen, w);
		        //Draw text
		        
		        g.drawString(text, x - diameter / 2, y + diameter / 2 + 10);


		        xwidth += lvlW;  // Increment xwidth for the next point
		    }

		    ylen += lvlLen;  // Move to the next level
		} 	

    }

    private void connectPoints(Graphics g, ArrayList<ArrayList<Object>> orderHass, int i, int j, int x, int y, int lvlLen, int w) 
	{
	    // Verify to which predecessor the point should be connected
	    boolean flag = false;
	    for (int k = 0; k < orderHass.get(i - 1).size(); k++) 
	    {
	        OrderedPair<T> ordpair = new OrderedPair<T>((T) orderHass.get(i - 1).get(k), (T) orderHass.get(i).get(j));
	        System.out.println(ordpair);

	        if (this.data.contains(ordpair)) 
	        {
	            paintLine(g, orderHass, w, k, i, y, x, lvlLen);
	            flag = true;

	        } 
	    }

	    if((i-2 >=0)&&!flag) 
	    {
	        // If a connection is not found on the current level, try searching on lower levels
	        connectPoints(g, orderHass, i - 1, j, x, y, lvlLen, w);
	    }
	}

    /*
    private void connectPoints(Graphics g, ArrayList<ArrayList<Object>> orderHass, int i, int j, int x, int y, int lvlLen, int w) 
    {
    	//Verify to which predecessor the point should be connected
    	//System.out.println(this.data);
    	for(int k = 0; k<orderHass.get(i-1).size(); k++)
    	{
    		OrderedPair<T> ordpair = new OrderedPair<T>((T)orderHass.get(i-1).get(k), (T)orderHass.get(i).get(j));
    		System.out.println(ordpair);
    		if(this.data.contains(ordpair))
    		{
    			paintLine(g, orderHass, w, k, i, y, x, lvlLen);
    		}
    		else
    		{

    		}
    	}
	}*/

	private void paintLine(Graphics g, ArrayList<ArrayList<Object>> orderHass, int w, int k, int i, int y, int x, int lvlLen)
	{
		System.out.println("Loop entered");
    	int lvlW = w / orderHass.get(i-1).size();
    	int prevX = k * lvlW + (lvlW / 2); ;
    	int prevY = y + lvlLen;
    	g.drawLine(prevX, prevY, x, y);
	}
}


class Matrix<T>
{
	int row, col;
	T[][] data;

	public Matrix(int r, int c)
	{
		this.row = r;
		this.col = c;
		this.data = (T[][]) new Object[r][c];
	}

	public void setElement(int r, int c, T value) 
	{
        if (r < 0 || r >= row || c < 0 || c >= col) 
	    {
	        throw new IndexOutOfBoundsException("Invalid row or column index");
	    }
	    data[r][c] = value;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol() 
    {
        return col;
    }

    // Get a specific element from the matrix

    public T getElement(int r, int c) 
    {
        if (r < 0 || r >= row || c < 0 || c >= col) 
        {
            System.out.println("Invalid row or column index");
        }
        return data[r][c];
    }

    // Method to print the matrix
    public void printMatrix() 
    {
    	int i,j;
        for (i = 0; i < row; i++) 
        {
            for (j = 0; j < col; j++) 
            {
                System.out.print(data[i][j] + " ");
            }
            System.out.println(); // Move to the next row
        }
    }

 
	//Transpose Matrix
	public Matrix<T> transpose()
	{
		int i,j;
		Matrix<T> t = new Matrix<T>(this.row, this.col);
		for (i = 0; i < this.row; i++) 
        {
            for (j = 0; j < this.col; j++) 
            {
                t.setElement(i, j, this.data[j][i]); //Change rows by columns
            }
        }

        return t;
	}

	public boolean equals(Matrix<T> b)
	{
		int i,j, cont = 0;

		if((this.row != b.row)||(this.col !=  b.col))
		{
			System.out.println("Impossible to compare, matrixes differ in size");
			return false;
		}
		for (i = 0; i < row; i++) 
        {
            for (j = 0; j < col; j++) 
            {
               if(this.data[i][j] == b.data[i][j])
               {
               		cont++;
               }
            }
        }
        return(cont == (this.row *this.col));
	}


}

class BoolMatrix extends Matrix<Integer>
{

	public BoolMatrix(int r, int c)
	{
		super(r,c);
	}

	public void setMatrix0()
	{
		//Initialize our matrix with 0s
		int i,j;
        for (i = 0; i < row; i++) 
        {
            for (j = 0; j < col; j++) 
            {
                this.setElement(i,j, 0);
            }
        }
	
	}

	public Integer getElement(int r, int c) 
    {
        return super.getElement(r,c);
    }


	//Matrix Multiplication
	public BoolMatrix product(BoolMatrix b)
	{
		int i,j,k;
		BoolMatrix mult = new BoolMatrix(this.row, b.col);
		//Initialize Matrix in 0s
		mult.setMatrix0();

		if(this.col != b.row)
		{
			System.out.println("Matrix dimensions are IncompatibleClassChangeError");
			return mult;
		}

		for(i=0; i<this.row; i++)
		{
			for(j=0; j<b.col; j++)
			{
				for(k=0;k<this.col;k++)
				{

					if((this.getElement(i,k) == 1)&&(b.getElement(k,j) == 1))
					{
						mult.setElement(i, j, 1);
						break;
					}
				}				
			}			
		}
		return mult;
	}

	public boolean minorEqual(BoolMatrix b)
	{
		int i,j,cont1=0, cont2=0;

		if((this.row != b.row)||(this.col != b.col))
		{
			System.out.println("Impossible to compare Matrixes, dimensions differ");
			return false;	
		}

		for(i=0; i<this.row; i++)
			{
				for(j=0; j<this.col; j++)
				{
					if(this.getElement(i,j) == 1)
					{
						cont1++;
						if(b.getElement(i,j) == 1)
						{
							cont2++;
						}
					} 
				}
			}

		return(cont1 == cont2);
		
	}

}


class OrderedPair<T>
{
    public T one; 
    public T two; 

    //Constructor
    public OrderedPair(T prim, T seg)
    {
        this.one = prim;
        this.two = seg;
    }

    //Method that obtains first component
    public T getOne()
    {
        return(one);
    }

    //Method that obtains second component
    public T getTwo()
    {
        return(two);
    }

    public boolean equals(OrderedPair<T> ordpair) 
    {
    	return((this.one.equals(ordpair.one))&&(this.two.equals(ordpair.two)));
	} 

    public String toString()
    {
        return("(" + one + "," + two + ")");
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) 
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) 
        {
            return false;
        }
        OrderedPair<?> that = (OrderedPair<?>) obj;
        return Objects.equals(one, that.one) && Objects.equals(two, that.two);
    }

    @Override
    public int hashCode() 
    {
        return Objects.hash(one, two);
    }

}


class DiscreteMath extends Frame
{
	
	
	public static PartialOrderR<String> PR;
	public static final int l=400;
	public static final int w=400;


	public static void main(String args[])
	{

		//Variables sets A,B,C,...
		Set<String> A = new Set<String>();
		Set<String> B = new Set<String>();

		//Define Relation
		Relation<String> R = new Relation<String>(A, B);

		//Define Matrix



		//Menu

		A.add("a");
		A.add("b");
		A.add("c");
		A.add("d");
		A.add("e");
		A.add("f");
		A.add("g");
		/*
		A.add("h");
		A.add("i");
		A.add("j");
		A.add("k"); */
		System.out.println(A);

		B.add("a");
		B.add("b");
		B.add("c");
		B.add("d");
		B.add("e");
		B.add("f");
		B.add("g");

		/*
		B.add("h");
		B.add("i");
		B.add("j");
		B.add("k"); */
		System.out.println(B);

		R.add(new OrderedPair<String>("a", "a"));
		R.add(new OrderedPair<String>("b", "b"));
		R.add(new OrderedPair<String>("c", "c"));
		R.add(new OrderedPair<String>("d", "d"));
		R.add(new OrderedPair<String>("e", "e"));
		R.add(new OrderedPair<String>("f", "f"));
		R.add(new OrderedPair<String>("g", "g"));

		/*
		R.add(new OrderedPair<String>("h", "h"));
		R.add(new OrderedPair<String>("i", "i"));
		R.add(new OrderedPair<String>("j", "j"));
		R.add(new OrderedPair<String>("k", "k")); */

		//R.add(new OrderedPair<String>("a", "b"));
		R.add(new OrderedPair<String>("e", "c"));
		R.add(new OrderedPair<String>("e", "b"));

		R.add(new OrderedPair<String>("f", "d"));
		R.add(new OrderedPair<String>("f", "a"));
		R.add(new OrderedPair<String>("f", "b"));

		R.add(new OrderedPair<String>("g", "e"));
		R.add(new OrderedPair<String>("g", "c"));
		R.add(new OrderedPair<String>("g", "b"));


		R.add(new OrderedPair<String>("d", "a"));


		R.add(new OrderedPair<String>("d", "b"));
		//R.add(new OrderedPair<String>("d", "f"));


		/*
		R.add(new OrderedPair<String>("a", "f"));
		R.add(new OrderedPair<String>("a", "g"));
		R.add(new OrderedPair<String>("a", "h"));
		R.add(new OrderedPair<String>("a", "i"));
		R.add(new OrderedPair<String>("a", "j"));
		R.add(new OrderedPair<String>("a", "k"));

		R.add(new OrderedPair<String>("b", "h"));
		R.add(new OrderedPair<String>("b", "i")); 

		R.add(new OrderedPair<String>("c", "g"));
		R.add(new OrderedPair<String>("c", "j"));
		R.add(new OrderedPair<String>("c", "f"));
		R.add(new OrderedPair<String>("c", "k"));
		R.add(new OrderedPair<String>("c", "i"));

		R.add(new OrderedPair<String>("d", "f"));
		R.add(new OrderedPair<String>("d", "k"));

		R.add(new OrderedPair<String>("e", "f"));
		R.add(new OrderedPair<String>("e", "k"));

		R.add(new OrderedPair<String>("h", "i"));

		R.add(new OrderedPair<String>("g", "i"));
		R.add(new OrderedPair<String>("g", "j"));

		R.add(new OrderedPair<String>("f", "k")); */
	


		System.out.println(R);

		/*
		System.out.println(A.union(B));
		System.out.println(A.intersect(B));
		System.out.println(A.cartprod(B));
		System.out.println(B.cartprod(A));
		System.out.println(A.difference(B));
		System.out.println(B.difference(A));
		*/
		System.out.println(R.functioncheck());
		R.relmatrix();
		R.mr.printMatrix();
		System.out.println("Reflexive"+R.isReflexive());
		System.out.println("Symmetric"+R.isSymmetric());
		System.out.println("Transitive"+R.isTransitive());
		System.out.println("Antisymmetric"+R.isAntisymmetric());
		System.out.println("Injective"+R.isInject());
		System.out.println("Surjective"+R.isSurject());
		System.out.println("Bijective"+R.isBiject());

		PR = new PartialOrderR<String>(R);

		ArrayList<String> isolatedv = PR.getIsoVert();

		//Imprimir vertices aislados
		int i;
		String s = "[";
		for(i=0; i<(isolatedv.size()); i++)
		{
			s = s + isolatedv.get(i);
			if(i<(isolatedv.size() - 1))
			{
				s = s + ", ";
			}
		}
		s = s + "]";
		System.out.println(s);

		//Pritn ordered Hass
		ArrayList<ArrayList<Object>> hass = PR.getHass();

		String h = "[";
		for(i=0; i<(hass.size()); i++)
		{
			h = h + "[";
			for(int j=0; j<(hass.get(i).size()); j++)
			{
				h = h + (String) hass.get(i).get(j);
				if(j<(hass.size() - 1))
				{
					h = h + ", ";
				}
			}
			h = h + "]";
		
			if(i<(hass.size() - 1))
			{
				h = h + ", ";
			}
		}
		h = h + "]";
		System.out.println(h);

		// Create an instance of the class
        DiscreteMath discreteMath = new DiscreteMath();

        // Set the size of the frame
        discreteMath.setSize(l, w);

        // Make the frame visible
        discreteMath.setVisible(true);

        // Add a window listener to handle window-closing event
        discreteMath.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });	




	}

	public void paint(Graphics g) 
    {
	    // Use the Graphics object passed to the paint method for drawing
       	PR.paintHass(g, l, w);
    }

}