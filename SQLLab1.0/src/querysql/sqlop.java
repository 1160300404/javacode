package querysql;

import java.sql.*;
import java.util.ArrayList;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class sqlop {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/sqllab?useSSL=false";
    static final String USER = "root";
    static final String PASS = "123456";
    static Connection conn = null;
    static Statement stmt = null;
    static PreparedStatement psql;
    private static ArrayList<String> result=new ArrayList<>();
    //初始化类开启与数据库的连接
    public static void init(){
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
    }
   //插入数据 分别是属性数量为2，3，4，5的表
    public static String insert(String tablename,String arg1 ,String arg2){
        sqlop.init();
        System.out.println(" 实例化Statement ");
        try {
            stmt = conn.createStatement();
            String sql;
            if(tablename.equals("department"))
            psql=conn.prepareStatement("insert into department (departid,dename)"+"values(?,?)");
            else if(tablename.equals("supplier"))
            psql=conn.prepareStatement("insert into supplier (suid,sname)"+"values(?,?)");
            else{
            	return "表与数据数目不对";
            }
            psql.setString(1, arg1);
            psql.setString(2, arg2);
            if(psql.executeUpdate()==1){
                conn.close();
                return "成功";
            }else{
                conn.close();
                return "操作失败";
            }
        } catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }catch (SQLException e) {
        	e.printStackTrace();
        	return "操作失败";
		}
    }
    public static String insert(String tablename,String arg1 ,String arg2,String arg3){
        sqlop.init();
        System.out.println(" 实例化Statement ");
        try {
            stmt = conn.createStatement();
            String sql;
            if(tablename.equals("intable"))
            psql=conn.prepareStatement("insert into intable (inid,houseid,departid)"+"values(?,?,?)");
            else if(tablename.equals("outtable"))
            psql=conn.prepareStatement("insert into outtable (outid,wid,departid)"+"values(?,?,?)");
            else if(tablename.equals("storerecord"))
            psql=conn.prepareStatement("insert into storerecord (warehouseid,goodsis,count)"+"values(?,?,?)");
            else{
            	return "表与数据数目不对";
            }
            psql.setString(1, arg1);
            psql.setString(2, arg2);
            psql.setString(3, arg3);
            if(psql.executeUpdate()==1){
                conn.close();
                return "成功";
            }else{
                conn.close();
                return "操作失败";
            }
        } catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }catch (SQLException e) {
            e.printStackTrace();
            return "操作失败";
        }
    }
    public static String insert(String tablename,String arg1 ,String arg2,String arg3,String arg4){
        sqlop.init();
        System.out.println(" 实例化Statement ");
        try {
            stmt = conn.createStatement();
            String sql;
            if(tablename.equals("goods"))
            psql=conn.prepareStatement("insert into goods (goodsid,goodsname,price,suid)"+"values(?,?,?,?)");
            else if(tablename.equals("warehouse"))
            psql=conn.prepareStatement("insert into warehouse (houseid,pid,housename)"+"values(?,?,?,?)");
            else if(tablename.equals("storerecord"))
            psql=conn.prepareStatement("insert into storerecord (warehouseid,goodsis,count)"+"values(?,?,?,?)");
            else{
            	return "表与数据数目不对";
            }
            psql.setString(1, arg1);
            psql.setString(2, arg2);
            psql.setString(3, arg3);
            psql.setString(4, arg3);
            if(psql.executeUpdate()==1){
                conn.close();
                return "成功";
            }else{
                conn.close();
                return "操作失败";
            }
        }catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
            return "操作失败";
        }
    }
    public static String insert(String tablename,String arg1 ,String arg2,String arg3,String arg4,String arg5){
        sqlop.init();
        System.out.println(" 实例化Statement ");
        try {
            stmt = conn.createStatement();
            String sql;
            if(tablename.equals("administrator"))
            psql=conn.prepareStatement("insert into administrator (pid,departid,name,age,sex)"+"values(?,?,?,?,?)"); 
            else{
            	return "表与数据数目不对";
            }
            psql.setString(1, arg1);
            psql.setString(2, arg2);
            psql.setString(3, arg3);
            psql.setString(4, arg4);
            psql.setString(5, arg5);
            if(psql.executeUpdate()==1){
                conn.close();
                return "成功";
            }else{
                conn.close();
                return "操作失败";
            }
        } catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }catch (SQLException e) {
            e.printStackTrace();
            return "操作失败";
        }
    }
  //删除数据 分别是属性数量为2，3，4，5的表   
    public static String delete(String tablename,String arg1,String arg2){
        sqlop.init();
        System.out.println(" 初始化Statement,数据库删除");
        try {stmt = conn.createStatement();
           if(tablename.equals("department"))
                psql = conn.prepareStatement("DELETE FROM department WHERE (departid like ?) and (dename like ?)");
           else if(tablename.equals("supplier"))
        	   psql = conn.prepareStatement("DELETE FROM supplier WHERE (suid like ?) and (sname like ?)");
           else{
           	return "表与数据数目不对";
           }  
           if(arg1!=null)
                psql.setString(1,arg1 );
        	   else {
				psql.setString(1, "%");
			}if(arg2!=null)
                psql.setString(2, arg2);
			else{
				 psql.setString(2, "%");
			}
                if (psql.executeUpdate() == 1) {
                    conn.close();
                    return "成功";
                } else {
                    conn.close();
                    return "操作失败";
                }
        } catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return "操作失败";
    }
    public static String delete(String tablename,String arg1,String arg2,String arg3){
        sqlop.init();
        System.out.println(" 初始化Statement,数据库删除");
        try {stmt = conn.createStatement();
        if(tablename.equals("intable"))
               psql = conn.prepareStatement("DELETE FROM intable WHERE (inid like ?) and (houseid like ?)and(departid like ?)");
        else if(tablename.equals("outtable"))
        	 psql = conn.prepareStatement("DELETE FROM outtable WHERE (outid like ?) and (wid like ?)and(departid like ?)");
        else if(tablename.equals("storerecord"))
        	psql = conn.prepareStatement("DELETE FROM storerecord WHERE (warehouseid like ?) and (goodsis like ?)and(count like ?)");
        else{
        	return "表与数据数目不对";
        }	  
        if(arg1!=null)
                psql.setString(1,arg1 );
        	   else {
				psql.setString(1, "%");
			}if(arg2!=null)
                psql.setString(2, arg2);
			else{
				 psql.setString(2, "%");
			}if(arg3!=null){
				psql.setString(3, arg3);
			}	else{
				 psql.setString(3, "%");
			}
                if (psql.executeUpdate() == 1) {
                    conn.close();
                    return "成功";
                } else {
                    conn.close();
                    return "操作失败";
                }
        } catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return "操作失败";
    }
    public static String delete(String tablename,String arg1,String arg2,String arg3,String arg4){
        sqlop.init();
        System.out.println(" 初始化Statement,数据库删除");
        try {stmt = conn.createStatement();
        if(tablename.equals("goods"))
               psql = conn.prepareStatement("DELETE FROM goods WHERE (goodsid like ?) and (goodsname like ?)and(price like ?)and(suid like ?)");
        else if(tablename.equals("warehouse"))
        	 psql = conn.prepareStatement("DELETE FROM warehouse WHERE (houseid like ?) and (pid like ?)and(housename like ?)and(departid like ?)");
        else{
        	return "表与数据数目不对";
        }	   
        if(arg1!=null)
                psql.setString(1,arg1 );
        	   else {
				psql.setString(1, "%");
			}if(arg2!=null)
                psql.setString(2, arg2);
			else{
				 psql.setString(2, "%");
			}if(arg3!=null){
				psql.setString(3, arg3);
			}	else{
				 psql.setString(3, "%");
			}if(arg4!=null){
				psql.setString(4, arg4);
			}	else{
				 psql.setString(4, "%");
			}
                if (psql.executeUpdate() == 1) {
                    conn.close();
                    return "成功";
                } else {
                    conn.close();
                    return "操作失败";
                }
        } catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return "操作失败";
    }
    public static String delete(String tablename,String arg1,String arg2,String arg3,String arg4,String arg5){
        sqlop.init();
        System.out.println(" 初始化Statement,数据库删除");
        try {stmt = conn.createStatement();
        if(tablename.equals("administrator"))
               psql = conn.prepareStatement("DELETE FROM administrator WHERE (pid like ?) and (departid like ?)and(name like ?)and(age like ?)and(sex like ?)");
        else{
        	return "表与数据数目不对";
        }
        	   if(arg1!=null)
                psql.setString(1,arg1 );
        	   else {
				psql.setString(1, "%");
			}if(arg2!=null)
                psql.setString(2, arg2);
			else{
				 psql.setString(2, "%");
			}if(arg3!=null){
				psql.setString(3, arg3);
			}	else{
				 psql.setString(3, "%");
			}if(arg4!=null){
				psql.setString(4, arg4);
			}	else{
				 psql.setString(4, "%");
			}if(arg5!=null){
				psql.setString(5, arg5);
			}	else{
				 psql.setString(5, "%");
			}
                if (psql.executeUpdate() == 1) {
                    conn.close();
                    return "成功";
                } else {
                    conn.close();
                    return "操作失败";
                }
        } catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return "操作失败";
    }

    //连接查询
    public static  ArrayList<String> connectSelect(String num){
    	result.clear();
    	sqlop.init();
         System.out.println(" 初始化Statement,数据库连接查询");
         try {stmt = conn.createStatement();
         String sql="Select * from warehouse natural join storerecord where count="+"'"+num+"'";
         ResultSet rs=stmt.executeQuery(sql);
         while(rs.next()){
        	 	StringBuilder stringBuilder=new StringBuilder();
        	 	stringBuilder.append(rs.getString("warehouseid")+" ");
        	 	stringBuilder.append(rs.getString("pid")+" ");
        	 	stringBuilder.append(rs.getString("housename")+" ");
        	 	stringBuilder.append(rs.getString("departid")+" ");
        	 	stringBuilder.append(rs.getString("goodsid")+" ");
        	 	result.add(stringBuilder.toString());
         }
         conn.close();
         return result;
         }catch(MySQLIntegrityConstraintViolationException e){
        	 System.out.println(e.getMessage());
        	 return null;
         } catch (SQLException e) {
			// TODO Auto-generated catch block
        	 System.out.println(e.getMessage());
			 return null;
		}
    }
    //分组查询
    public static  ArrayList<String> embedSelect(String name){
    	result.clear();
    	sqlop.init();
         System.out.println(" 初始化Statement,数据库删除");
         try {stmt = conn.createStatement();
         String sql="Select * from outtable where wid in (Select warehouseid from warehouse where pid in (Select pid from administrator where name = "+"'"+name+"'"+"))";
        ResultSet rs=stmt.executeQuery(sql);
         while(rs.next()){
     	 	StringBuilder stringBuilder=new StringBuilder();
     	 	stringBuilder.append(rs.getString("outid")+" ");
     	 	stringBuilder.append(rs.getString("wid")+" ");
     	 	stringBuilder.append(rs.getString("departid")+" ");
     	 	result.add(stringBuilder.toString());
      }
         conn.close();
         return result;
         }catch(MySQLIntegrityConstraintViolationException e){
        	 System.out.println(e.getMessage());
        	 return null;
         } catch (SQLException e) {
			// TODO Auto-generated catch block
        	 System.out.println(e.getMessage());
			 return null;
		}
    }
    public static  ArrayList<String> groupSelect(String name){
    	result.clear();
    	sqlop.init();
         System.out.println(" 初始化Statement,数据库");
         try {stmt = conn.createStatement();
         String sql="Select AVG(age) from administrator where departid =  "+"'"+name+"'"+" Group by name";
        ResultSet rs=stmt.executeQuery(sql);
        System.out.println(sql);
         while(rs.next()){
     	 	StringBuilder stringBuilder=new StringBuilder();
     	 	stringBuilder.append(rs.getString("AVG(age)")+" ");
     	 	result.add(stringBuilder.toString());
      }
         conn.close();
         return result;
         }catch(MySQLIntegrityConstraintViolationException e){
        	 System.out.println(e.getMessage());
        	 return null;
         } catch (SQLException e) {
			// TODO Auto-generated catch block
        	 System.out.println(e.getMessage());
			 return null;
		}
    }
    public static ArrayList<String> useview(){
    	result.clear();
        sqlop.init();
        System.out.println(" 初始化Statement,数据库");
        try {
        	String sql="select dename,housename from 部门与对应仓库";
        	stmt=conn.createStatement();
        	ResultSet rs=stmt.executeQuery(sql);
        	while(rs.next()){
        		StringBuilder stringBuilder=new StringBuilder();
         	 	stringBuilder.append(rs.getString("dename")+" ");
         	 	stringBuilder.append(rs.getString("housename")+" ");
         	 	result.add(stringBuilder.toString());
        	}
        	conn.close();
            return result;
        } catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println(e.getMessage());
           
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<String> useindex(){
    	result.clear();
        sqlop.init();
        System.out.println(" 初始化Statement,数据库");
        try {
        	String sql="select outid,wid from outtable where departid = '0304'";
        	stmt=conn.createStatement();
        	ResultSet rs=stmt.executeQuery(sql);
        	while(rs.next()){
        		StringBuilder stringBuilder=new StringBuilder();
         	 	stringBuilder.append(rs.getString("outid")+" ");
         	 	stringBuilder.append(rs.getString("wid")+" ");
         	 	result.add(stringBuilder.toString());
        	}
        	conn.close();
            return result;
        } catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println(e.getMessage());
           
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
