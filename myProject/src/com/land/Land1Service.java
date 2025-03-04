package com.land;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


import landutils.LandManager;

public class Land1Service {
	
	Scanner scanner = new Scanner(System.in);
	
	private boolean insertlogin = false;
	private String member_id = null;

	public boolean getInsertLogin( ) {
		return insertlogin;
	}
	
	public int insertlogin() {
		
		System.out.println("아이디: ");
		member_id = scanner.nextLine();
		
		System.out.println("비밀번호: ");
		String member_pw = scanner.nextLine();
		
		Connection conn = LandManager.getConnection();
		String loginsql = "SELECT COUNT(*) FROM landmember WHERE member_id = ? AND member_pw = ?";
		
		PreparedStatement pstmt = null;
        ResultSet rs = null;
		
        try {
        	pstmt = conn.prepareStatement(loginsql);
        	pstmt.setString(1, member_id);
        	pstmt.setString(2, member_pw);
        	rs = pstmt.executeQuery();
        	
        	if(rs.next() && rs.getInt(1) > 0) {
        		insertlogin = true;
        		System.out.println("로그인에 성공했습니다");
        	}else {
        		System.out.println("아이디 또는 비밀번호가 잘못되었습니다");
        	}
        }catch(SQLException se) {
        	System.out.println("로그인 오류: " + se.getMessage());
        } finally {
            LandManager.dbClose(conn, pstmt, rs);
        }
		return 0;
	}


	// 지번 제목
	public int insertBoard() {
		System.out.print("지번등록: \n(취소: Y) ");
		String street = scanner.nextLine();
		if(street.equalsIgnoreCase("Y")) { 
			System.out.println("작성이 취소되었습니다.");
			return -1;
		}else if(street.length() > 10) {
			System.out.println("입력가능한 글자를 초과했습니다");
			return -1;
		}
	// 지번 내용
		System.out.print("주소: \n(취소: Y)");
		String address = scanner.nextLine();
		if(address.equalsIgnoreCase("Y")) {
			System.out.println("작성이 취소되었습니다");
			return -1;
		}else if(street.length() > 10) {
			System.out.println("입력가능한 글자를 초과했습니다");
			return -1;
		}
		
		System.out.print("지목: \n(취소: Y)");
		String category = scanner.nextLine();
		if(category.equalsIgnoreCase("Y")) {
			System.out.println("작성이 취소되었습니다");
			return -1;
		}else if(street.length() > 10) {
			System.out.println("입력가능한 글자를 초과했습니다");
			return -1;
		}
		
		System.out.print("면적: \n(취소: Y)");
		String area = scanner.nextLine();
		if(area.equalsIgnoreCase("Y")) {
			System.out.println("작성이 취소되었습니다");
			return -1;
		}else if(street.length() > 10) {
			System.out.println("입력가능한 글자를 초과했습니다");
			return -1;
		}
		
		System.out.print("공시지가: \n(취소: Y)");
		String price = scanner.nextLine();
		if(price.equalsIgnoreCase("Y")) {
			System.out.println("작성이 취소되었습니다");
			return -1;
		}else if(street.length() > 10) {
			System.out.println("입력가능한 글자를 초과했습니다");
			return -1;
		}
			
		Connection conn = LandManager.getConnection();
		
		
		String insertsql = "INSERT INTO landuse (seq, street, address, category, area, price) VALUES (seq_landuse_No.nextval, ?, ?, ?, ?, ?)";
		
		int resultRow = 0;
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(insertsql);
			pstmt.setString(1, street);		// 위 insertsql에 첫번째 ?에 키보드로 입력받은 제목을 세팅
			pstmt.setString(2, address);
			pstmt.setString(3, category);
			pstmt.setString(4, area);
			pstmt.setString(5, price);
			
			resultRow = pstmt.executeUpdate();
			
			System.out.println("글 성공적으로 등록됨");
		} catch(SQLException se) {
			System.out.println("삽입하는 도중 에러가 발생 ->" + se.getMessage());
		} finally {
			// 7. Oracle DB접속 관련 객체들 정리
			LandManager.dbClose(conn, pstmt, null);
		}
		return resultRow;
	}
				
	public void allBoard() {
		System.out.println("--------------------------------------------------------------");
		System.out.println("번호         지번                                주소           ");
		System.out.println("--------------------------------------------------------------");
		
		Connection conn = LandManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String insertsql = "SELECT seq, street, address FROM landuse ORDER BY seq DESC";
		int countRow = 0;
		
		try {
			pstmt = conn.prepareStatement(insertsql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				countRow++;
				
			System.out.println(
					String.format("%-12s", rs.getString("seq")) + 
					String.format("%-34s", rs.getString("street")) + 
					String.format("%-10s", rs.getString("address"))
					);
			}
			if(countRow == 0) {
				System.out.println("지번 목록이 존재하지 않습니다.");
			}
		}catch(SQLException se) {
			System.out.println("목록 조회하는 도중 에러가 발생 -> " + se.getMessage());
		}finally {
			// Oracle DB 접속 관련 객체들 정리
			LandManager.dbClose(conn, pstmt, rs);
			}
	  }
	
	private int getBoardNO() {
		int boardNO = 0;
		while(true) {
		System.out.println("지번 찾기 번호를 입력하세요.> ");
		
			try {
				boardNO = Integer.parseInt(scanner.nextLine());
				break;
			}catch(InputMismatchException | NumberFormatException e) {
				System.out.println("잘못된 값을 입력하셨습니다. 다시 입력해주세요.");
			}catch(Exception e) {
				System.out.println("알 수 없는 오류 ->" + e.getMessage());
				}
		}
		return boardNO;
	}
			
	public void oneBoard() {
		
		int boardNo = getBoardNO();
		Connection conn = LandManager.getConnection();
		
		String countsql = "SELECT COUNT(*) FROM landuse WHERE seq = ?";
		
		String detailsql = "SELECT * FROM landuse WHERE seq = ?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(countsql);
			pstmt.setInt(1, boardNo);
			pstmt.setInt(1, boardNo);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int rowCount = rs.getInt(1);		// count(*) 값 가져오기
				
				if(rowCount == 0) {			// 상세조회할 게시글이 없는 경우
					System.out.println("상세조회할 지번 번호가 없습니다.");
					return;
				}
			}
			
			pstmt = conn.prepareStatement(detailsql);
			pstmt.setInt(1, boardNo);
			rs = pstmt.executeQuery();
			
			String url = "https://www.eum.go.kr/web/am/amMain.jsp";
			
			if(rs.next()) {		// sql 실행한 데이터들의 1행을 가져옴(데이터가 있으면 true, 없으면 false)
				// 4. sql실행해서 가져온 데이터들을 자바변수에 세팅 혹은 출력
				// 5. 게시판 상세조회 출력 화면
				System.out.println("-------------------------------------------------------------");
				System.out.println("번호: " + rs.getInt("seq"));
				System.out.println("지번: " + rs.getString("street"));
				System.out.println("주소: " + rs.getString("address"));
				System.out.println("지목: " + rs.getString("category"));
				System.out.println("면적: " + rs.getInt("area") + " ㎡");
				System.out.println("공시지가: " + rs.getInt("price") + "(만)원");
				System.out.println("-------------------------------------------------------------");
				
				
				System.out.println(url + "링크로 들어가시겠습니까? (Y|N)");
				String linkedurl = scanner.nextLine();
				if(!linkedurl.equalsIgnoreCase("Y")) 
					return;
				
				if (Desktop.isDesktopSupported()) {
	                Desktop desktop = Desktop.getDesktop();
	                try {
	                    desktop.browse(new URI(url));
	                } catch (IOException | URISyntaxException e) {
	                    System.out.println("웹 브라우저 열기 오류: " + e.getMessage());
	                }
	            } else {
	                System.out.println("Desktop class is not supported on this platform.");
	            }
			}
			
		}catch(SQLException se) {
			System.out.println("상세조회 쿼리 실행 오류: " + se.getMessage());
		} finally {
			LandManager.dbClose(conn, pstmt, rs);
			}
	}
	
	public void updateBoard() {
		int boardNO = getBoardNO();
		
		String countSql = "SELECT COUNT(*) FROM landuse WHERE seq = ?";
		
		Connection conn = LandManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int resultRow = 0;
		try {
			
			pstmt = conn.prepareStatement(countSql);
			pstmt.setInt(1, boardNO);
			rs = pstmt.executeQuery(); // 
			
			if(rs.next()) {
				resultRow = rs.getInt(1);
				if (resultRow == 0) {
					System.out.println("없는 번호입니다.");
					return;
				}
			}
		} catch(SQLException se) {
			System.out.println("번호 개수 쿼리 실행 오류: " + se.getMessage());
		}
		
		System.out.println("수정할 지번을 입력하세요.> ");
		String updatestreet = scanner.nextLine();
		
		System.out.println("수정할 주소를 입력하세요.> ");
		String updateaddress = scanner.nextLine();
		
		System.out.println("수정할 지목를 입력하세요.> ");
		String updatecategory = scanner.nextLine();
		
		System.out.println("수정할 면적를 입력하세요.> ");
		String updatearea = scanner.nextLine();
		
		System.out.println("수정할 가격를 입력하세요.> ");
		String updateprice = scanner.nextLine();
		
		
		String updateOneSql = "UPDATE landuse SET street = ?, address = ?, category = ?, area = ?, price = ? WHERE seq = ?";
		
		try {
			pstmt = conn.prepareStatement(updateOneSql);
			pstmt.setString(1, updatestreet);
			pstmt.setString(2, updateaddress);
			pstmt.setString(3, updatecategory);
			pstmt.setString(4, updatearea);
			pstmt.setString(5, updateprice);
			pstmt.setInt(6, boardNO);
			resultRow = pstmt.executeUpdate();		
					
			System.out.println("번호 " + boardNO + "번을 성곡적으로 수정하였습니다.");
		} catch(SQLException se) {
			System.out.println("수정 쿼리 실행 오류: " + se.getMessage());
		} finally {
			LandManager.dbClose(conn, pstmt, rs);
		}
	}
	
	public void deleteBoard() {
		int boardNO = getBoardNO();
		
		Connection conn = LandManager.getConnection();
		
		String countsql = "SELECT COUNT(*) FROM landuse where seq = ?";
		
		String deletesql = "DELETE FROM landuse where seq = ?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int resultRow = 0;
		
		try {
			pstmt = conn.prepareStatement(countsql);
			pstmt.setInt(1, boardNO);
			rs = pstmt.executeQuery();		// Select 실행
			if(rs.next()) {
				resultRow = rs.getInt(1);
				
				if(resultRow == 0) {
					System.out.println("삭제할 지번 번호가 없습니다.");
					return;
				}
			}
		
		System.out.println(boardNO + "삭제하시겠습니까? (Y|N)");
		String confirmYN = scanner.nextLine();
		if(!confirmYN.equals("Y"))
			return;
			
			pstmt = conn.prepareStatement(deletesql);
			pstmt.setInt(1, boardNO);
			pstmt.executeUpdate();		// Select 실행
			
			System.out.println("번호 " + boardNO + "번을 성곡적으로 삭제하였습니다.");
		}catch(SQLException se) {
			System.out.println("삭제 쿼리 실행 오류: " + se.getMessage());
		} finally {
			LandManager.dbClose(conn, pstmt, rs);
		}
	}
	
	
	public int createId() {
		System.out.print("아이디: \n(취소: Y)");
		String member_id = scanner.nextLine();
		if(member_id.equalsIgnoreCase("Y")) {
			System.out.println("작성이 취소되었습니다");
			return -1;
		}else if(member_id.length() > 30) {
			System.out.println("입력가능한 글자를 초과했습니다");
			return -1;
		}
		
		System.out.print("비밀번호: \n(취소: Y)");
		String member_pw = scanner.nextLine();
		if(member_pw.equalsIgnoreCase("Y")) {
			System.out.println("작성이 취소되었습니다");
			return -1;
		}else if(member_pw.length() > 30) {
			System.out.println("입력가능한 글자를 초과했습니다");
			return -1;
		}
		
		System.out.print("이름: \n(취소: Y)");
		String memeber_name = scanner.nextLine();
		if(memeber_name.equalsIgnoreCase("Y")) {
			System.out.println("작성이 취소되었습니다");
			return -1;
		}else if(memeber_name.length() > 30) {
			System.out.println("입력가능한 글자를 초과했습니다");
			return -1;
		}
		
		System.out.print("전화번호: \n(취소: Y)");
		String member_phone = scanner.nextLine();
		if(member_phone.equalsIgnoreCase("Y")) {
			System.out.println("작성이 취소되었습니다");
			return -1;
		}else if(member_phone.length() > 12) {
			System.out.println("입력가능한 글자를 초과했습니다");
			return -1;
		}
			
		Connection conn = LandManager.getConnection();
		
		String checksql = "SELECT COUNT(*) FROM landmember WHERE member_id = ?";
		String insertsql = "INSERT INTO landmember (member_seq, member_id, member_pw, memeber_name, member_phone) VALUES (seq_memberuse_No.nextval, ?, ?, ?, ?)";
		String checknember = "SELECT COUNT(*) FROM landmember WHERE member_phone = ?";
		
		int resultRow = 0;
		
		PreparedStatement pstmtCheck = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			
		
		try {
		 pstmtCheck = conn.prepareStatement(checksql);
	     pstmtCheck.setString(1, member_id);
	     rs = pstmtCheck.executeQuery();
	        if (rs.next() && rs.getInt(1) > 0) {
	            System.out.println("이미 사용중인 아이디입니다.");
	            return -1;
	            }
         pstmtCheck = conn.prepareStatement(checknember);
	     pstmtCheck.setString(1, member_phone);
	     rs = pstmtCheck.executeQuery();
	        if (rs.next() && rs.getInt(1) > 0) {
	            System.out.println("이미 사용중인 전화번호입니다.");
	            return -1;
	            }
			
			pstmt = conn.prepareStatement(insertsql);
			pstmt.setString(1, member_id);
			pstmt.setString(2, member_pw);
			pstmt.setString(3, memeber_name);
			pstmt.setString(4, member_phone);
			
			resultRow = pstmt.executeUpdate();
				
				if(resultRow == 0) {
					System.out.println("회원가입을 할 수 없습니다.");
					return - 1;
				}
			
			System.out.println("회원가입이 성공적으로 마쳤습니다."); 
			
		}catch(SQLException se) {
			System.out.println("회원가입 오류: " + se.getMessage());
		} finally {
			LandManager.dbClose(conn, pstmt, null);
		}
		return resultRow;
	}
}