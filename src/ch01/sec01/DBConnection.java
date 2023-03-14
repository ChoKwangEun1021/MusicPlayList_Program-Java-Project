package ch01.sec01;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class DBConnection {

	private Connection connection = null;
	public void connect() {
		Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("C:\\2023.01.Java Warkspace\\Music\\src\\ch01\\sec01\\db.properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("FileInputStream error" + e.getStackTrace());
		} catch (IOException e) {
			System.out.println("Properties.load error" + e.getStackTrace());
		}
		try {
			// 1. jdbc Ŭ���� �ε�
			Class.forName(properties.getProperty("driverName"));
			// 2. mysql DB ����
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"),
					properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("[�����ͺ��̽� �ε����]" + e.getStackTrace());
		} catch (SQLException e) {
			System.out.println("[�����ͺ��̽� �������]" + e.getStackTrace());
		}
	}

	// �����ͻ���
	public int insert(Music m) {
		// 3. �����͸� �����Ѵ�. insert into ���̺�� (�ʵ�,...) values(_,_,_);
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
		String query = "insert into musictbl values(null, ?, ?, ?)";

		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, m.getTitle());
			ps.setString(2, m.getSinger());
			ps.setString(3, m.getGenre());
			// ���� �����ϸ� 1 ����
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert �����߻�" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
			}
		} // end of finally
		return returnValue;
	}

	// ���� select statement
	public ArrayList<Music> select() {
		ArrayList<Music> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from musictbl";

		try {
			ps = connection.prepareStatement(query);
			// select �����ϸ� ResultSet���� ����, �����ϸ� null
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String singer = rs.getString("singer");
				String genre = rs.getString("genre");
				list.add(new Music(id, title, singer, genre));
			}
		} catch (Exception e) {
			System.out.println("select �����߻�" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
			}
		} // end of finally
		return list;
	}

	// �м����� analize Select statement
	public ArrayList<Music> analizeSelect() {
		ArrayList<Music> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select id, title, singer, genre from musictbl";

		try {
			ps = connection.prepareStatement(query);
			// select �����ϸ� ResultSet���� ����, �����ϸ� null
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String singer = rs.getString("singer");
				String genre = rs.getString("genre");
				list.add(new Music(id, title, singer, genre));
			}
		} catch (Exception e) {
			System.out.println("select �����߻�" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
			}
		} // end of finally
		return list;
	}

	// �̸��˻�����
	public ArrayList<Music> songSearchSelect(String dataTitle) {
		ArrayList<Music> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from musictbl where title like ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, "%" + dataTitle + "%");
			// select �����ϸ� ResultSet���� ����, �����ϸ� null
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String singer = rs.getString("singer");
				String genre = rs.getString("genre");
				list.add(new Music(id, title, singer, genre));
			}
		} catch (Exception e) {
			System.out.println("select �����߻�" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
			}
		} // end of finally
		return list;
	}

	// select id
	public Music selectId(int dataId) {
		Music music = null;
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from musictbl where id = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, dataId);
			// select �����ϸ� ResultSet���� ����, �����ϸ� null
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			if (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String singer = rs.getString("singer");
				String genre = rs.getString("genre");
				music = new Music(id, title, singer, genre);
			}
		} catch (Exception e) {
			System.out.println("select id �����߻�" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
			}
		} // end of finally
		return music;
	}

	// update
	public int update(Music m) {
		// 3. �����͸� �����Ѵ�. insert into ���̺�� (�ʵ�,...) values(_,_,_);
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
		String query = "update musictbl set title = ?, singer = ?, genre = ? where id = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, m.getTitle());
			ps.setString(2, m.getSinger());
			ps.setString(3, m.getGenre());
			ps.setInt(4, m.getId());
			// ���� �����ϸ� 1 ����
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("update �����߻�" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
			}
		} // end of finally
		return returnValue;
	}

	public ArrayList<Music> selectSort() {
		ArrayList<Music> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from musictbl order by title desc";

		try {
			ps = connection.prepareStatement(query);
			// select �����ϸ� ResultSet���� ����, �����ϸ� null
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String singer = rs.getString("singer");
				String genre = rs.getString("genre");
				list.add(new Music(id, title, singer, genre));
			}
		} catch (Exception e) {
			System.out.println("selectsort �����߻�" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
			}
		} // end of finally
		return list;
	}

	public int delete(int deleteId) {
		// 3. �����͸� �����Ѵ�. insert into ���̺�� (�ʵ�,...) values(_,_,_);
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
		String query = "delete from musictbl where id = ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, deleteId);

			// ���� �����ϸ� 1 ����
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert �����߻�" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
			}
		} // end of finally
		return returnValue;
	}

	public int deleteAll() {
		// 3. �����͸� �����Ѵ�. insert into ���̺�� (�ʵ�,...) values(_,_,_);
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
		String query = "delete from musictbl";

		try {
			ps = connection.prepareStatement(query);

			// ���� �����ϸ� 1 ����
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert �����߻�" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close ����" + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("connection close ����" + e.getMessage());
			}
		} // end of finally
		return returnValue;
	}

}
