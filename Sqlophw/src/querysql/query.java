package querysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class query {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/sqloplab?useSSL=false";
    static final String USER = "root";
    static final String PASS = "123456";
    static Connection conn = null;
    static Statement stmt = null;
    static PreparedStatement psql;
    private static String sid;
    private static String sname;
    private static String ssex;
    private static String scalss;
    private static String sdept;
    private static String saddr;
    private static String beage;
    private static String enage;
    private static ArrayList<ArrayList<String>> result=new ArrayList<>();
    //初始化类开启与数据库的连接
    public static void init(String sid,String sname,String sclass,String sdept,String ssex,String beage,String enage,String saddr){
        try{
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
        //初始化数据
    	query.sid=sid;
    	query.beage=beage;
    	query.enage=enage;
    	query.saddr=saddr;
    	query.sname=sname;
    	query.ssex=ssex;
    	query.sdept=sdept;
    	query.scalss=sclass;
    }
    public  static String setSqlsen(){
    	String sqlse="select * from student where ";
    	StringBuilder stringBuilder=new StringBuilder(sqlse);
    	if(sid!=null){
    		stringBuilder.append("(sid like '"+sid+"') and");
    	}if(sname!=null){
    		stringBuilder.append("(sname like '"+sname+"') and");
    	}if(scalss!=null){
    		stringBuilder.append("(sclass like '"+scalss+"') and");
    	}if(sdept!=null){
    		stringBuilder.append("(sdept like '"+sdept+"') and");
    	}if(saddr!=null){
    		stringBuilder.append("(saddr like '"+saddr+"') and");
    	}if(ssex!=null){
    		stringBuilder.append("(ssex ='"+ssex+"') and");
    	}if(beage!=null&&enage!=null){
    		stringBuilder.append("(sage >="+beage+")and (sage <="+enage+")and");
    	}
    	if(!stringBuilder.toString().equals(sqlse))
    	return stringBuilder.substring(0, stringBuilder.length()-3);
    	else
    		return "select * from student ";
    }
    public static ArrayList<ArrayList<String>> doquery(){
    	result.clear();
    	String sql=setSqlsen();//创建查找语句
    	 sqlop.init();
         System.out.println(" 实例化Statement对象...查询信息");
         try {
             stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);//执行
           while(rs.next()){
                ArrayList<String > arrayList=new ArrayList<>();
                arrayList.add(rs.getString("sid"));
                arrayList.add(rs.getString("sname"));
                arrayList.add(rs.getString("sage"));
                arrayList.add(rs.getString("ssex"));
                arrayList.add(rs.getString("sclass"));
                arrayList.add(rs.getString("sdept")); 
                arrayList.add(rs.getString("saddr"));
                result.add(arrayList);
                 
             }
           conn.close();
         } catch (SQLException e) {
             e.printStackTrace();
             try {
                 conn.close();
             } catch (SQLException e1) {
                 e1.printStackTrace();
             }
             return null;
         }
    	return result;
    }
}
