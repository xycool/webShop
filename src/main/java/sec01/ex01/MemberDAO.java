package sec01.ex01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {
	/*
	// For Orcale
	//private static final String driver = "oracle.jdbc.driver.OracleDriver";
	//private static final String url = "jdbc:oracle:thin:@localhost:1521:XE";
	
	// For Sql Server
	private static final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String url = "jdbc:sqlserver://localhost:1433;database=webShop";
	
	private static final String user = "webShop";
	private static final String pwd = "1234";
	
	*/
	
	private Connection con;
	//private Statement stmt;
	private PreparedStatement pstmt;
	private DataSource dataFactory;
	
	
	
	public MemberDAO() {
		try {
			// Tomcat context.xml에 Resource 설정이 되어 있어야 함.
			/*
			 <Resource
		    	name="jdbc/SqlServer"
		    	auth="Container"
		    	type="javax.sql.DataSource"
		    	driverClassName="com.microsoft.sqlserver.jdbc.SQLServerDriver"
		    	url="jdbc:sqlserver://localhost:1433;database=webShop"
		    	username="webShop"
		    	password="1234"
		    	maxActive="50"
		    	maxWait="-1" />
			 */
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/SqlServer");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 멤버 등록
	 * @param memberVO
	 */
	public void addMember(MemberVO memberVO) {
		try {
			con = dataFactory.getConnection();
			String id = memberVO.getId();
			String pwd = memberVO.getPwd();
			String name = memberVO.getName();
			String email = memberVO.getEmail();
			String query = "insert into t_member";
			query += " (id,pwd,name,email)";
			query += " values(?,?,?,?)";
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			pstmt.setString(3, name);
			pstmt.setString(4, email);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delMember(String id) {
		try {
			con = dataFactory.getConnection();
			String query = "delete from t_member" + " where id=?";
			System.out.println("prepareStatememt:" + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 회원 list
	 * @return list
	 */
	public List<MemberVO> listMembers()  {
		
		List list = new ArrayList();
		
		try {
			//connDB();
			
			con = dataFactory.getConnection();
			
			String query = "select * from t_member where ? = ?";
			
			//System.out.println(query);
			//ResultSet rs = stmt.executeQuery(query);
			
			System.out.println("prepareStatememt: " + query);
			
			pstmt = con.prepareStatement(query);
			
			pstmt.setInt(1, 1);
			pstmt.setInt(2, 1);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String id = rs.getString("id");
				String pwd = rs.getString("pwd");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date joinDate = rs.getDate("joinDate");
				
				MemberVO vo = new MemberVO();
				
				vo.setId(id);
				vo.setPwd(pwd);
				vo.setName(name);
				vo.setEmail(email);
				vo.setJoinDate(joinDate);
				
				list.add(vo);
			}
			rs.close();
			//stmt.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return list;
	}
	/*
	private void connDB() {
		try {
			Class.forName(driver);
			System.out.println("데이타베이스 드라이버 로딩 성공");
			con = DriverManager.getConnection(url, user, pwd);
			System.out.println("Connection 생성 성공");
			//stmt = con.createStatement();
			//pstmt = con.createStatement();
			//System.out.println("Statement 생성 성공");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	*/
}
