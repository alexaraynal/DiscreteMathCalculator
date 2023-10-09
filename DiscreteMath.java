import java.util.*; //I/O

class Set<T> //T represents type
{
	ArrayList<T> data;

	public Set()
	{
		data = new ArrayList<>();	
	}

	public void add(T element)
	{
		if(data.contains(data) == false)
		{
			data.add(element);
		}
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
	}

	//Function or not
	public boolean functioncheck()
	{
		int i;

		//Check cardinality
		if(this.data.size() == a.data.size())
		{
			for(i=0; i<(a.data.size()); i++)
			{
				if(a.search(this.data.get(i).getOne()) != -1)
				{
					return(true);
				}
				else return(false);
			}
		}
		return(false);
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
            System.out.println("Invalid row or column index");
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
    }


class DiscreteMath
{
	//Menu

	public static void main(String args[])
	{

		//Variables sets A,B,C,...
		Set<String> A = new Set<String>();
		Set<String> B = new Set<String>();

		//Define Relation
		Relation<String> R = new Relation<String>(A, B);

		//Define Matrix



		//Menu

		A.add("4");
		A.add("5");
		A.add("6");
		A.add("7");
		System.out.println(A);

		B.add("4");
		B.add("5");
		B.add("6");
		B.add("7");
		System.out.println(B);

		R.add(new OrderedPair<String>("4", "4"));
		R.add(new OrderedPair<String>("5", "5"));
		R.add(new OrderedPair<String>("6", "6"));
		R.add(new OrderedPair<String>("7", "7"));

		System.out.println(R);


		System.out.println(A.union(B));
		System.out.println(A.intersect(B));
		System.out.println(A.cartprod(B));
		System.out.println(B.cartprod(A));
		System.out.println(A.difference(B));
		System.out.println(B.difference(A));
		System.out.println(R.functioncheck());
		R.relmatrix();
		R.mr.printMatrix();
		System.out.println("Reflexive"+R.isReflexive());
		System.out.println("Symmetric"+R.isSymmetric());
		System.out.println("Transitive"+R.isTransitive());
		System.out.println("Antisymmetric"+R.isAntisymmetric());
		




	}
}