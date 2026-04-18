import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class JDBC {
    public static void main(String[] args) throws SQLException {
        Scanner s = new Scanner(System.in);
        System.out.println("Conecting to database\nEnter DataBase name: ");
        String dbName = s.nextLine(); //AirportGC
        System.out.println("Enter Database username(default: postgres): ");
        String dbUsername = s.nextLine();
        System.out.println("Enter DataBase password: ");
        String dbPassword = s.nextLine();

        Connection connection = null;
        String dbUrl = "jdbc:postgresql://localhost:5432/" +dbName+"";

        //Set Connection
        connection = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
        
        //Statement
        Statement statement = connection.createStatement();
        

        //Main menu
        Boolean menuRunning = true;

        while (menuRunning) {
            System.out.println("\nPick an option.\n" +
                                "1. View Current table.\n" +
                                "2. Add new Data to table.\n" +
                                "3. remove Data from table.\n" + 
                                "4. Change Data in table.\n" +
                                "5. Select a different option.\n" +
                                "6. Delete all data from table.\n" +
                                "7. Exit programm.\n");
            Scanner scn = new Scanner(System.in);
            int option = scn.nextInt();
                
            switch (option) {
                case 1: //SELECT *

                    String selectQuery = "SELECT * FROM gc";
                    ResultSet resultSet1 = statement.executeQuery(selectQuery);
                
                    while(resultSet1.next()){
                    System.out.println("\n"+ resultSet1.getString("flight_id") + " - " + resultSet1.getString("plane_id") + 
                                        " - " + resultSet1.getString("plane_status") + 
                                        " - " + resultSet1.getString("gate") + 
                                        " - " + resultSet1.getString("route") + 
                                        " - " + resultSet1.getTime("time") + 
                                        " - " + resultSet1.getDate("date"));
                    }
                    
                    continue;
                case 2: //INSERT new flight

                    System.out.println("\nEnter flight id: ");
                    scn.nextLine();
                    String flight_id = scn.nextLine();
                    System.out.println("Enter plane id: ");
                    String plane_id = scn.nextLine();
                    System.out.println("Enter plane status (Arriving/Boarding/Departuring): ");
                    String plane_status = scn.nextLine();
                    System.out.println("Enter gate(arr/dep): ");
                    String gate = scn.nextLine();
                    System.out.println("Enter route(arr/dep): ");
                    String route = scn.nextLine();
                    System.out.println("Enter time(arr/dep) in format HH:MM : ");
                    String time_str = scn.nextLine();
                    LocalTime time = LocalTime.parse(time_str);
                    System.out.println("Enter date(arr/dep) in format yyyy-mm-dd: ");
                    String date_str = scn.nextLine();
                    LocalDate date = LocalDate.parse(date_str);
                    String insertQuery = "INSERT INTO gc(flight_id,plane_id,plane_status,gate,route,time,date) VALUES(?,?,?,?,?,?,?)";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        preparedStatement.setString(1,flight_id);
                        preparedStatement.setString(2,plane_id);
                        preparedStatement.setString(3,plane_status);
                        preparedStatement.setString(4,gate);
                        preparedStatement.setString(5,route);
                        preparedStatement.setObject(6,time);
                        preparedStatement.setObject(7,date);

                        preparedStatement.executeUpdate();
                        System.out.println("\nRow added successfully");
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                    
                    continue;

                case 3://REMOVE single id

                    System.out.println("\nEnter flight ID to remove from table: ");
                    scn.nextLine();
                    String idToRemove = scn.nextLine();
                    String removeRowQuery = "DELETE FROM gc WHERE flight_id = "+"'" +idToRemove+"'";
                    statement.executeUpdate(removeRowQuery);
                    System.out.println("\nRow with flight_id: "+idToRemove+" was removed.");

                    continue;
                
                    case 4://ALTER data

                        System.out.println("Enter flight ID to change value: ");
                        scn.nextLine();
                        String idToAlter = scn.nextLine();
                        System.out.println("Enter column name to change it's value [flight_id/plane_id/plane_status/gate/route/time/date]: ");
                        String colToAlter = scn.nextLine();
                        System.out.println("Enter new data to replace with [formats: time- hh:mm/date- yyyy-mm-dd]: ");
                        String replacementData = scn.nextLine();
                        
                        String alterDataQuery;

                        if (colToAlter == "date") {
                            LocalDate replacementDate = LocalDate.parse(replacementData);
                            alterDataQuery = "UPDATE gc SET " +colToAlter+ " = "+"'" +replacementDate+"'"+ " WHERE flight_id = "+"'" +idToAlter+"'";
                        }
                        else if (colToAlter == "time"){
                            LocalTime replacemenTime = LocalTime.parse(replacementData);
                            alterDataQuery = "UPDATE gc SET " +colToAlter+ " = "+"'" +replacemenTime+"'"+ " WHERE flight_id = "+"'" +idToAlter+"'";
                        }
                        else{
                            alterDataQuery = "UPDATE gc SET " +colToAlter+ " = "+"'" +replacementData+"'"+ " WHERE flight_id = "+"'" +idToAlter+"'";
                        }
                        statement.executeUpdate(alterDataQuery);
                        System.out.println("\nThe change was successful.");

                        continue;

                case 5:
                    continue;

                case 6: //Remove all table data
                    String deleteAllDataQuery = "DELETE FROM gc";
                    statement.executeUpdate(deleteAllDataQuery);
                    System.out.println("\nAll Data  from table was removed.");

                    continue;

                case 7: //exit
                    System.out.println("\ngood Bye!");
                    menuRunning = false;
  
            }
            scn.close();
        }
        //Close connection
        
        connection.close();

    }
}
