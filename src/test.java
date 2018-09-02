import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Scanner;


public class test {
	static Statement statement;//statement variable used to execute sql statements
	static Connection connection;//used to connect to sqlite database
	public static void connect() throws ClassNotFoundException {//function to connect to database
		Class.forName("org.sqlite.JDBC");
		connection = null;//initialising connection 
		try
		{
			connection = DriverManager.getConnection("jdbc:sqlite:finance.db");//connecting to database with finance data 

			connection.setAutoCommit(false);//automatic commit is set to false 
			statement = connection.createStatement();//initialising statement
			statement.setQueryTimeout(40);
		}
		catch(SQLException e)
		{
			System.out.println("Connection failed!");
		}
	}
	public static void disconnect() throws SQLException {//function to disconnect from database
		if(connection != null)
			connection.close();

	}
	public static void sort(String sortchoice, String formatchoice)//function to sort data and display
	{
		String sql = "SELECT * FROM finance" +
				" ORDER BY "+ sortchoice + " " + formatchoice;

		ResultSet rs;
		try {
			rs = statement.executeQuery(sql);//querying database using sql statement
			connection.commit();//committing to database
			display(rs);//displaying results
			
		} catch (SQLException e) {
			try {
				connection.rollback();//rollback if execution is not successful 
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Incorrect entry!");
		}


	}
	public static void display(ResultSet rs) //function to display results retrieved from database
	{
		
			try {
				while(rs.next())
				{
					System.out.println(rs.getString("Segment") + "		" + rs.getString("Country") + "		" + rs.getString("Product") + "		" + rs.getString("DiscountBound") + "		" + rs.getInt("UnitsSold") + "		" + rs.getString("ManufacturingPrice")
					+ "		" + rs.getString("SalePrice") + "		" + rs.getString("GrossSales") + "		" + rs.getString("Discounts") + "		" + rs.getString("Sales") + "		" + rs.getString("COGS") + "		" + rs.getString("Profit") + "		" + rs.getString("Date") 
					+ "		"+rs.getInt("MonthNumber") + "		"+ rs.getString("MonthName") + "		" + rs.getInt("Year"));
				}
			} catch (SQLException e) {
				System.out.println("Corresponding data does not exist");
			}
			System.out.println("\n");
		
	}


	public static void insert(String Segment, String Country, String Product, String DiscountBound, String UnitsSold, String ManufacturingPrice, String SalePrice, String GrossPrice, String Discounts, String Sales, String Cogs, String Profit,String Date, String MonthName, String MonthNumber, String Year)  {
		//function to insert or append data to  database
		try {
			statement.execute("INSERT INTO finance " + 
					"VALUES ('" + Segment +"', '" + Country +"', '" +Product + "', '"+DiscountBound +"', '"+ Integer.parseInt(UnitsSold) +"', '"+ ManufacturingPrice +"', '"+ SalePrice +"', '"+ GrossPrice +"', '"+ Discounts +"', '"+ Sales +"', '"+  Cogs +"', '"+ Profit +"', '"+ Date +"', '"+ Integer.parseInt(MonthNumber) +"', '"+ MonthName+"', '"+Integer.parseInt(Year) +"');");
			connection.commit();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Wrong value entered (type-mismatch)! Insertion unsuccessful!\n");
		}
	}

	public static void delete(String condition)  {//function to delete data from database

		try {
			statement.execute("Delete from finance where " + condition);
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Field or value not found");
		} 
	}
	private static void search(String condition) {//function to search for data in database
		// TODO Auto-generated method stub
		ResultSet rs;
		try {
			rs = statement.executeQuery("select * from finance where " +condition);
			connection.commit();
			display(rs);
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Error! Incorrect Condition");
		}

	}
	public static void update(String value,  String condition) {//function to update data in database
		try {
			statement.execute("UPDATE finance set " + value + " WHERE " + condition);
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				
			}
			System.out.println("Wrong value entered");
		} 
	}

	public static void displayAllData() throws ClassNotFoundException
	{//function to display all data in database

		ResultSet rs;
		try {
			rs = statement.executeQuery("select * from finance");
			connection.commit();
			display(rs);
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException
	{//main program
		connect();//connect to database
		boolean newAction = true;//set new action to true
		while(newAction)
		{

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Welcome to my data visualiser. Which action would you like to carry out? Enter a letter e.g a, b, c\n");

			System.out.println("A. Search	B. Sort		C. Insert	D. Update	E. Delete\n");

			System.out.println("F. Display All Data		G. Append\n");
			System.out.println("SQL type statements are used in this application thus, values are case sensitive, and string types would need to be enclosed with single quotation marks\n");
			
			boolean optionIncorrect = true;//initialise option to true
			String str= "", c = "";
			while(optionIncorrect)
			{
				Scanner s = new Scanner(System.in);
				c = s.nextLine();
				str = c.toLowerCase();
				if(str.length()!= 1)//check if length of option is 1
				{
					System.out.println("Choice is invalid. Try again \n");
				}
				else if(str.equals( "a") || str.equals( "b")  || str.equals( "c")  || str.equals( "d")  || str.equals( "e")  || str.equals( "f")  || str.equals( "g") )
				{//check if entered value is either a or b or c or d or e or f or g
					optionIncorrect = false;
					System.out.println("You have chosen " + c.toUpperCase() + "\n");
				}
				else//otherwise choice is incorrect
				{
					System.out.println("Choice is invalid. Try hghghjh again \n");

				}
			}
			if(str.equals("a"))//search option was chosen
			{
				System.out.println("What do you want to search for? Enter a condition eg country = 'Canada', year > 2014, String types are case sensitive and quotation marks are needed for them \n");
				String value = in.readLine(); 
				search(value);//call search function 
			}
			else if(str.equals("b"))//sort option was chosen 
			{
				System.out.println("How do you want data to be sorted? \n");

				System.out.println("Date	Country		Segment		Product		UnitsSold		ManufacturingPrice		SalePrice		GrossSales		Sales		Cogs		Profit 		Year		Month\n");
				String sortchoice = in.readLine(); 
				System.out.println("Asc or Desc\n");

				String formatchoice = in.readLine(); 
				sort(sortchoice, formatchoice);//call sort function
			}
			else if(str.equals("c") || str.equals("g"))//insert or append option was chosen
			{	
				System.out.println("You have chosen to insert/append data. Please enter values for the following fields: \n");

				System.out.print("Segment: ");
				String Segment = in.readLine(); 

				System.out.print("Country: ");
				String Country = in.readLine(); 

				System.out.print("Product: ");
				String Product = in.readLine(); 

				System.out.print("DiscountBound: ");
				String DiscountBound = in.readLine(); 

				System.out.print("UnitsSold: ");
				String UnitsSold = in.readLine(); 

				System.out.print("ManufacturingPrice: ");
				String ManufacturingPrice = in.readLine(); 

				System.out.print("SalePrice: ");
				String SalePrice = in.readLine(); 
				
				System.out.print("GrossPrice: ");
				String GrossPrice = in.readLine(); 

				System.out.print("Discounts: ");
				String Discounts = in.readLine(); 

				System.out.print("Sales: ");
				String Sales = in.readLine(); 
				
				System.out.print("Cogs: ");
				String Cogs = in.readLine(); 

				System.out.print("Profit: ");
				String Profit = in.readLine(); 

				System.out.print("MonthName: ");
				String MonthName = in.readLine(); 

				System.out.print("Date: ");
				String Date = in.readLine(); 

				System.out.print("MonthNumber: ");
				String MonthNumber = in.readLine(); 

				System.out.print("Year: ");
				String Year = in.readLine(); 
				
				
				//call insert function 
				insert(Segment, Country, Product, DiscountBound, UnitsSold, ManufacturingPrice, SalePrice, GrossPrice, Discounts, Sales, Cogs, Profit, Date, MonthName, MonthNumber, Year);
			}
			else if(str.equals("d"))//update option was chosen 
			{
				System.out.println("You have chosen to update data. Please enter the value to be updated e.g product = 'Montana' as well as the condition e.g year = 2013");
				System.out.print("Value: ");

				String value = in.readLine(); 
				System.out.print("Condition: ");
				String condition = in.readLine(); 
				update(value, condition);//call update function 
			}
			else if(str.equals("e"))//delete option was chosen 
			{
				System.out.println("You have chosen to delete data. Please enter a condition eg year = 2014");
				String condition = in.readLine(); 
				delete(condition);//call delete function 
				
			}
			else if(str.equals("f"))//display all data function was chosen 
			{
				displayAllData();//call function 
			}
			System.out.print("Do you want to take another action? y/n:");//prompt user to continue 
			String choice = in.readLine();
			if(choice.equals("y"))//if yes, loop back 
				newAction = true;
			else if (choice.equals("n"))//else, terminate program 
			{
				newAction = false;
				disconnect();
				System.out.print("Thank you. Program terminated!\n");
			}
			else//if wrong choice was entered, terminate program 
			{
				newAction = false; 
				disconnect();
				System.out.print("Incorrect choice. Program terminated!\n");
			}

		}
	}
	



}
