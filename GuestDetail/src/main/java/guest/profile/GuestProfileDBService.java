package guest.profile;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
public class GuestProfileDBService {
	Connection con;
	
	
	public GuestProfileDBService()
	{
		DBConnectionDTO conDTO = new DBConnectionDTO();
		con=conDTO.getConnection();
	}
	
	public void closeConnection()
	{
		try {
			con.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
public int getTotalPages(int limit)
	{
		String query="select count(*) from guest_profile";
	    int totalRecords=0;
	    int totalPages=0;
		try {
		Statement stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery(query);
	    if(rs.next()) {
	    	totalRecords= rs.getInt(1);
	    }
	    stmt.close();
	    rs.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
		totalPages=totalRecords/limit;
		if(totalRecords%limit!=0)
		{
			totalPages+=1;
		}
		return totalPages;
	}
	
	//pagination
	public int getTotalPages(GuestProfile guestProfile,int limit)
	{
		String query=getDynamicQuery2(guestProfile);
		int totalRecords=0;
	    int totalPages=0;
		try {
		Statement stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery(query);
	    if(rs.next()) {
	    	totalRecords= rs.getInt(1);
	    }
	    stmt.close();
	    rs.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
		totalPages=totalRecords/limit;
		if(totalRecords%limit!=0)
		{
			totalPages+=1;
		}
		return totalPages;
	}
	
	
	public int getguestProfileId(GuestProfile guestProfile)
	{
		int id=0;
		String query="select id from guest_profile";
String whereClause = " where "+ "name=? and age=?";
	    query+=whereClause;
		System.out.println(query);
		try {
PreparedStatement pstmt = con.prepareStatement(query);
pstmt.setString(1, guestProfile.getName());
pstmt.setInt(2, guestProfile.getAge());
	    ResultSet rs = pstmt.executeQuery();
	    if(rs.next()) {
	       	id = rs.getInt("id");
	    }
		}
	    catch (Exception e) {
	    	System.out.println(e);
		}
	    
	    return id;
	}
	public void createguestProfile(GuestProfile guestProfile)
	{
		
String query="INSERT INTO guest_profile(name,age) VALUES(?,?)";
	
    System.out.println(query);
		try {
PreparedStatement pstmt = con.prepareStatement(query);
pstmt.setString(1, guestProfile.getName());
pstmt.setInt(2, guestProfile.getAge());
	    int x = pstmt.executeUpdate();
	    }
	    catch (Exception e) {
	  
  	System.out.println(e);
		}
		int id = getguestProfileId(guestProfile);
		guestProfile.setId(id);
	}
	public void updateguestProfile(GuestProfile guestProfile)
	{
		
String query="update guest_profile set "+"name=?,age=? where id=?";
	   
 System.out.println(query);
		try {
PreparedStatement pstmt = con.prepareStatement(query);
pstmt.setString(1, guestProfile.getName());
pstmt.setInt(2, guestProfile.getAge());
pstmt.setInt(3, guestProfile.getId());
	    int x = pstmt.executeUpdate();
	    }
	    catch (Exception e) {
	    	System.out.println(e);
		}
		
	}
	public String getValue(String code,String table) {
		
		String value="";
		String query="select value from "+table+" where code='"+code+"'";
	    System.out.println(query);
		try {
		Statement stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery(query);
	    if(rs.next()) {
	    	
	    	value=rs.getString("value");
	    }
		}
		catch (Exception e) {
			System.out.println(e);
		}
	    return value;
	}
	
	public GuestProfile getguestProfile(int id)
	{
		GuestProfile guestProfile =new GuestProfile();
		String query="select * from guest_profile where id="+id;
	    System.out.println(query);
		try {
		Statement stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery(query);
	    if(rs.next()) {
	    	
	
guestProfile.setId(rs.getInt("id")==0?0:rs.getInt("id"));
guestProfile.setName(rs.getString("name")==null?"":rs.getString("name"));
guestProfile.setAge(rs.getInt("age")==0?0:rs.getInt("age"));
	    	
	    }
		}
	    catch (Exception e) {
	    	System.out.println(e);
		}
	    
	    return guestProfile;
	}
	
	
	public ArrayList<GuestProfile> getguestProfileList()
	{
		ArrayList<GuestProfile> guestProfileList =new ArrayList<GuestProfile>();
		String query="select * from guest_profile";
	    System.out.println(query);
		try {
		Statement stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery(query);
	    while(rs.next()) {
	    	GuestProfile guestProfile =new GuestProfile();
guestProfile.setId(rs.getInt("id")==0?0:rs.getInt("id"));
guestProfile.setName(rs.getString("name")==null?"":rs.getString("name"));
guestProfile.setAge(rs.getInt("age")==0?0:rs.getInt("age"));
	    	guestProfileList.add(guestProfile);
	    }
		}
	    catch (Exception e) {
	    	System.out.println(e);
		}
	    
	    return guestProfileList;
	}
	
	public ArrayList<GuestProfile> getguestProfileList(int pageNo,int limit)
	{
		ArrayList<GuestProfile> guestProfileList =new ArrayList<GuestProfile>();
String query="select * from guest_profile limit "+limit +" offset "+limit*(pageNo-1);
	    System.out.println(query);
		try {
		Statement stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery(query);
	    while(rs.next()) {
	    	GuestProfile guestProfile =new GuestProfile();
guestProfile.setId(rs.getInt("id")==0?0:rs.getInt("id"));
guestProfile.setName(rs.getString("name")==null?"":rs.getString("name"));
guestProfile.setAge(rs.getInt("age")==0?0:rs.getInt("age"));
	    	guestProfileList.add(guestProfile);
	    }
		}
	    catch (Exception e) {
	    	System.out.println(e);
		}
	    
	    return guestProfileList;
	}
	
	public void deleteguestProfile(int id) {
		
			String query="delete from guest_profile where id="+id;
		    System.out.println(query);
				
			
		    try {
			Statement stmt = con.createStatement();
		    int x = stmt.executeUpdate(query);
		    }
		    catch (Exception e) {
		    	System.out.println(e);
			}
		
	}
	
public String getDynamicQuery(GuestProfile guestProfile)
{
String query="select * from guest_profile ";
String whereClause="";
whereClause+=(guestProfile.getAge()==0?"":" age="+guestProfile.getAge());
if(!whereClause.equals(""))
query+=" where "+whereClause;
System.out.println("Search Query= "+query);
    return query;
}
public String getDynamicQuery2(GuestProfile guestProfile)
{
String query="select count(*) from guest_profile ";
String whereClause="";
whereClause+=(guestProfile.getAge()==0?"":" age="+guestProfile.getAge());
if(!whereClause.equals(""))
query+=" where "+whereClause;
System.out.println("Search Query= "+query);
    return query;
}
public ArrayList<GuestProfile> getguestProfileList(GuestProfile guestProfile)
{
ArrayList<GuestProfile> guestProfileList =new ArrayList<GuestProfile>();
String query=getDynamicQuery(guestProfile);
System.out.println("Search Query= "+query);
try {
Statement stmt = con.createStatement();
ResultSet rs = stmt.executeQuery(query);
while(rs.next()) {
	GuestProfile guestProfile2 =new GuestProfile();
guestProfile2.setId(rs.getInt("id")==0?0:rs.getInt("id"));
guestProfile2.setName(rs.getString("name")==null?"":rs.getString("name"));
guestProfile2.setAge(rs.getInt("age")==0?0:rs.getInt("age"));
    	guestProfileList.add(guestProfile2);
    }
	}
    catch (Exception e) {
    	System.out.println(e);
	}
    return guestProfileList;
}
	
public ArrayList<GuestProfile> getguestProfileList(GuestProfile guestProfile,int pageNo,int limit)
{
ArrayList<GuestProfile> guestProfileList =new ArrayList<GuestProfile>();
String query=getDynamicQuery(guestProfile);
query+= " limit "+limit +" offset "+limit*(pageNo-1);
System.out.println("Search Query= "+query);
try {
Statement stmt = con.createStatement();
ResultSet rs = stmt.executeQuery(query);
while(rs.next()) {
	GuestProfile guestProfile2 =new GuestProfile();
guestProfile2.setId(rs.getInt("id")==0?0:rs.getInt("id"));
guestProfile2.setName(rs.getString("name")==null?"":rs.getString("name"));
guestProfile2.setAge(rs.getInt("age")==0?0:rs.getInt("age"));
    	guestProfileList.add(guestProfile2);
    }
	}
    catch (Exception e) {
    	System.out.println(e);
	}
    return guestProfileList;
}
	
	
	public static void main(String[] args) {
		
		GuestProfileDBService guestProfileDBService =new GuestProfileDBService();
		
		
		
		 //Test-1 : Create guestProfile
		  
		GuestProfile guestProfile = new GuestProfile(); guestProfile.setDefaultValues();
		  guestProfileDBService.createguestProfile(guestProfile);
		  
		 ArrayList<GuestProfile> guestProfileList = guestProfileDBService.getguestProfileList();
		 GuestProfileService guestProfileService =new GuestProfileService();
		guestProfileService.displayList(guestProfileList);
		
	}
}

