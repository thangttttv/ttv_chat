package com.ttv.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.ttv.bean.MessageData;
import com.ttv.bean.Offer;
import com.ttv.bean.Product;
import com.ttv.bean.SwapType;
import com.ttv.bean.User;
import com.ttv.chat.SwapChatSeverHandler;


public class SwapHubDAO {
	final static Logger logger = Logger.getLogger(SwapHubDAO.class);
	
	public int saveSMSAlert() {
		PreparedStatement ps= null;
		PreparedStatement statement =  null;
		Connection connection = null;
		ResultSet rs =  null;
		int id = 0;
		try {
			connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
					.prepareStatement("INSERT INTO ms_user_mobile "
							+ "(user_id, mobile, code_validate, create_date, "
							+ "update_date, is_sended, type_message) "
							+ " VALUES (0, '84974838181',  '', NOW(), "
							+ "NOW(), 0, 1);");
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			String sql = "SELECT LAST_INSERT_ID()";
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			if (rs.next())
				id = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(statement);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return id;
	}
	
	
	public int saveNotify(int object_id,int to_user,int from_user,String content
			,String icon,String url,int object_type,String create_user) {
		PreparedStatement ps= null;
		PreparedStatement statement =  null;
		Connection connection = null;
		ResultSet rs =  null;
		int id = 0;
		try {
			connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
					.prepareStatement("INSERT INTO ms_notify  (object_id, to_user, from_user, "
							+ "content, icon, url, object_type, STATUS, time_sent, create_date, create_user )"
							+ "VALUES (?, ?, ?, "
							+ "?,?,?, ?, 1, NOW(),NOW(),?);");
			ps.setInt(1, object_id);
			ps.setInt(2, to_user);
			ps.setInt(3, from_user);
			ps.setString(4, content);
			ps.setString(5, icon);
			ps.setString(6, url);
			ps.setInt(7, object_type);
			ps.setString(8, create_user);
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			String sql = "SELECT LAST_INSERT_ID()";
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			if (rs.next())
				id = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(statement);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return id;
	}
	
	public int updateNotify(int notify_id,String content) {
		PreparedStatement ps= null;
		Connection connection = null;
		int id = 0;
		try {
			connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
					.prepareStatement("Update  ms_notify  SET content = ?, create_date = NOW(), STATUS = 1 Where id = ? ");
			ps.setString(1, content);
			ps.setInt(2, notify_id);
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return id;
	}
	
	public int saveMessageQueue(MessageData chatMessage,int app_client_id) {
		PreparedStatement ps= null;
		PreparedStatement statement =  null;
		Connection connection = null;
		ResultSet rs =  null;
		int id = 0;
		try {
			connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
					.prepareStatement("INSERT INTO ms_chat_message_queue "
							+ " (app_client_id,fuID,tuID,pID,p_swap_ids,content,price, "
							+ " quantity,file_path,type_message,time_receive,time_send)"
							+ " VALUES(?,?,?,?,?,?,?,?,?,?,NOW(),?)");
			ps.setInt(1, app_client_id);
			ps.setInt(2, chatMessage.getFuID());
			ps.setInt(3, chatMessage.getTuID());
			ps.setInt(4, chatMessage.getpID());
			ps.setString(5, chatMessage.getP_swap_id());
			ps.setString(6, chatMessage.getContent());
			ps.setDouble(7, chatMessage.getPrice());
			ps.setInt(8, chatMessage.getQuatity());
			ps.setString(9, chatMessage.getFile());
			ps.setInt(10, chatMessage.getType().getByteValue());
			ps.setLong(11,chatMessage.getTime());
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			String sql = "SELECT LAST_INSERT_ID()";
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			if (rs.next())
				id = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(statement);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return id;
	}
	
	public int saveMessageQueue(MessageData chatMessage) {
		PreparedStatement ps= null;
		PreparedStatement statement =  null;
		Connection connection = null;
		ResultSet rs =  null;
		int id = 0;
		try {
			connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
					.prepareStatement("INSERT INTO ms_chat_message_queue "
							+ " (app_client_id,fuID,tuID,pID,p_swap_ids,content,price, "
							+ " quantity,file_path,type_message,time_receive,time_send)"
							+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
			ps.setInt(1, 0);
			ps.setInt(2, chatMessage.getFuID());
			ps.setInt(3, chatMessage.getTuID());
			ps.setInt(4, chatMessage.getpID());
			ps.setString(5, chatMessage.getP_swap_id());
			ps.setString(6, chatMessage.getContent());
			ps.setDouble(7, chatMessage.getPrice());
			ps.setInt(8, chatMessage.getQuatity());
			ps.setString(9, chatMessage.getFile());
			ps.setInt(10, chatMessage.getType().getByteValue());
			ps.setLong(11,Calendar.getInstance().getTimeInMillis());
			ps.setLong(12,chatMessage.getTime());
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			String sql = "SELECT LAST_INSERT_ID()";
			statement = connection.prepareStatement(sql);
			rs= statement.executeQuery();
			if (rs.next())
				id = rs.getInt(1);

			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(statement);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return id;
	}
	
	
	public int saveMessage(MessageData chatMessage) {
		PreparedStatement ps= null;
		PreparedStatement statement =  null;
		Connection connection = null;
		ResultSet rs =  null;
		int id = 0;
		try {
			connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
					.prepareStatement("INSERT INTO ms_chat_message "
							+ " (fuID,tuID,pID,p_swap_ids,content,price, "
							+ " quantity,file_path,type_message,time_receive,time_send)"
							+ " VALUES(?,?,?,?,?,?,?,?,?,NOW(),?)");

			ps.setInt(1, chatMessage.getFuID());
			ps.setInt(2, chatMessage.getTuID());
			ps.setInt(3, chatMessage.getpID());
			ps.setString(4, chatMessage.getP_swap_id());
			ps.setString(5, chatMessage.getContent());
			ps.setDouble(6, chatMessage.getPrice());
			ps.setInt(7, chatMessage.getQuatity());
			ps.setString(8, chatMessage.getFile());
			ps.setInt(9, chatMessage.getType().getByteValue());
			ps.setLong(10,chatMessage.getTime());
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			String sql = "SELECT LAST_INSERT_ID()";
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			if (rs.next())
				id = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(statement);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return id;
	}
	
	public int deleteMessageQueue(int id) {
		PreparedStatement ps= null;
		Connection connection = null;
		int kq = 0;
		try {
			connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
					.prepareStatement("DELETE FROM ms_chat_message_queue WHERE id =  "
							+ id);
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			kq = 1;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return kq;
	}
	
	public int deleteMessageQueue(int fuid,int tuid,int pid,long timesend) {
		PreparedStatement ps= null;
		Connection connection = null;
		int kq = 0;
		try {
			connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection
					.prepareStatement("DELETE FROM ms_chat_message_queue WHERE "
							+ "  fuID = ? And tuID= ? AND pID= ? And time_send = ? "
							);
			ps.setInt(1, fuid);
			ps.setInt(2, tuid);
			ps.setInt(3, pid);
			ps.setLong(4, timesend);
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			kq = 1;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return kq;
	}

	public ArrayList<MessageData> getListMessageQueue(int userID) {
		PreparedStatement ps= null;
		MessageData chatMessage = null;
		Connection connection =  null;
		ResultSet rs = null;
		ArrayList<MessageData> listMatch = new ArrayList<MessageData>();
		try {
			connection = C3p0SwapHubPool.getConnection();
			ps = connection.prepareStatement("SELECT id,fuID, tuID, pID, p_swap_ids, content, "
							+ "price, quantity,ship,file_path,type_message,time_receive,time_send FROM "
							+ "ms_chat_message_queue Where tuID = "
							+ userID + "  Order by id   ");
			rs = ps.executeQuery();
			while (rs.next()) {
				chatMessage = new MessageData();
				chatMessage.id = rs.getInt("id");
				chatMessage.fuID = rs.getInt("fuID");
				chatMessage.tuID = rs.getInt("tuID");
				chatMessage.pID = rs.getInt("pID");
				chatMessage.p_swap_id = rs.getString("p_swap_ids");
				chatMessage.content = rs.getString("content");
				chatMessage.price = rs.getDouble("price");
				chatMessage.quantity = rs.getInt("quantity");
				chatMessage.file = rs.getString("file_path");
				
				byte type = rs.getByte("type_message");
				chatMessage.setType(SwapType.fromByte(type));
				
				chatMessage.time_receive = rs.getLong("time_receive");
				chatMessage.time = rs.getLong("time_send");
				listMatch.add(chatMessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return listMatch;
	}
	
	public ArrayList<MessageData> getListMessageOffLive(int userID) {
		PreparedStatement ps= null;
		MessageData chatMessage = null;
		Connection connection = null;
		ResultSet rs =  null; 
		ArrayList<MessageData> listMatch = new ArrayList<MessageData>();
		try {
			connection = C3p0SwapHubPool.getConnection();

			ps = connection.prepareStatement("SELECT id,fuID, tuID, pID, p_swap_ids, content, "
							+ "price, quantity,ship,file_path,type_message,time_receive,time_send FROM "
							+ "ms_chat_message_queue Where tuID = "
							+ userID + "  Order by id   ");
			rs = ps.executeQuery();
			while (rs.next()) {
				chatMessage = new MessageData();
				chatMessage.id = rs.getInt("id");
				chatMessage.fuID = rs.getInt("fuID");
				chatMessage.tuID = rs.getInt("tuID");
				chatMessage.pID = rs.getInt("pID");
				chatMessage.p_swap_id = rs.getString("p_swap_ids");
				chatMessage.content = rs.getString("content");
				chatMessage.price = rs.getDouble("price");
				chatMessage.quantity = rs.getInt("quantity");
				chatMessage.file = rs.getString("file_path");
				
				byte type = rs.getByte("type_message");
				chatMessage.setType(SwapType.fromByte(type));
				
				chatMessage.time_receive = rs.getLong("time_receive");
				chatMessage.time = rs.getLong("time_send");
				listMatch.add(chatMessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return listMatch;
	}
	
	
	
	public ArrayList<MessageData> getListMessageQueue(int userID,int app_client_id) {
		Connection connection  = null;
		PreparedStatement ps  = null;
		ResultSet rs   = null;
		MessageData chatMessage = null;
		ArrayList<MessageData> listMatch = new ArrayList<MessageData>();
		try {
			connection = C3p0SwapHubPool.getConnection();
			ps = connection.prepareStatement("SELECT id,fuID, tuID, pID, p_swap_ids, content, "
							+ "price, quantity,ship,file_path,type_message,time_receive,time_send FROM "
							+ "ms_chat_message_queue Where (tuID = "
							+ userID + " OR fuID = "+userID+"  ) And app_client_id = "+app_client_id+" Order by id  LIMIT 0, 100 ");
			rs = ps.executeQuery();
			while (rs.next()) {
				chatMessage = new MessageData();
				chatMessage.id = rs.getInt("id");
				chatMessage.fuID = rs.getInt("fuID");
				chatMessage.tuID = rs.getInt("tuID");
				chatMessage.pID = rs.getInt("pID");
				chatMessage.p_swap_id = rs.getString("p_swap_ids");
				chatMessage.content = rs.getString("content");
				chatMessage.price = rs.getDouble("price");
				chatMessage.quantity = rs.getInt("quantity");
				chatMessage.file = rs.getString("file_path");
				
				byte type = rs.getByte("type_message");
				chatMessage.setType(SwapType.fromByte(type));
				
				chatMessage.time_receive = rs.getLong("time_receive");
				chatMessage.time = rs.getLong("time_send");
				listMatch.add(chatMessage);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return listMatch;
	}
	
	
	
	public User getUserLogin(int userID, String username) {
		PreparedStatement ps=null;
		Connection connection=null;
		ResultSet rs = null;
		User user = null;
		try {
			connection = C3p0SwapHubPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM ms_user  WHERE   id = ? AND username = ? ");
			ps.setInt(1, userID);
			ps.setString(2, username);
			rs = ps.executeQuery();
			if (rs.next()) {
				user = new User();
				user.id = rs.getInt("id");
				user.username = rs.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return user;
	}
	
	public User getUserBuyID(int userID) {
		PreparedStatement ps=null;
		Connection connection=null;
		ResultSet rs = null;
		User user = null;
		try {
			connection = C3p0SwapHubPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM ms_user  WHERE   id = ?  ");
			ps.setInt(1, userID);
			rs = ps.executeQuery();
			if (rs.next()) {
				user = new User();
				user.id = rs.getInt("id");
				user.username = rs.getString("username");
				user.fullname = rs.getString("fullname");
				user.avatar_url = rs.getString("avatar_url");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return user;
	}
	
	public Product getProductByID(int product_id) {
		PreparedStatement ps=null;
		Connection connection=null;
		ResultSet rs = null;
		Product product = null;
		try {
			connection = C3p0SwapHubPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM ms_product  WHERE   id = ?  ");
			ps.setInt(1, product_id);
			rs = ps.executeQuery();
			if (rs.next()) {
				product = new Product();
				product.id = rs.getInt("id");
				product.user_id = rs.getInt("user_id");
				product.cate_id = rs.getInt("cate_id");
				product.cate_parent_id = rs.getInt("cate_parent_id");
				product.title = rs.getString("title");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return product;
	}
	
	public List<Integer> getUserFollowerSendNotify(int user_id){
		PreparedStatement ps=null;
		Connection connection=null;
		ResultSet rs = null;
		ArrayList<Integer> listUserID = new ArrayList<Integer>();
		try {
			connection = C3p0SwapHubPool.getConnection();
			ps = connection.prepareStatement("SELECT user_id FROM ms_user_following  WHERE user_following_id = ?");
			ps.setInt(1, user_id);
			rs = ps.executeQuery();
			while (rs.next()) {
				listUserID.add(rs.getInt("user_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return listUserID;
	}
	
	public List<Integer> getUserLikeProduct(int product_id,String userIDNotIn){
		PreparedStatement ps=null;
		Connection connection=null;
		ResultSet rs = null;
		ArrayList<Integer> listUserID = new ArrayList<Integer>();
		
		String sql = "SELECT user_id FROM   ms_product_favorite WHERE product_id =   "+ product_id;
		if(!(userIDNotIn==null&&"".equalsIgnoreCase(userIDNotIn)))
	        sql = "SELECT user_id FROM  ms_product_favorite WHERE product_id = "+product_id+" and user_id not in ("+userIDNotIn+") ";
		
		try {
			connection = C3p0SwapHubPool.getConnection();
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				listUserID.add(rs.getInt("user_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return listUserID;
	}
	
	
	public void pushNotifySold(int product_id,int buyer_id){
	    try {
	    	 Product product = getProductByID(product_id);
		      int from_user = product.user_id;
		      
		      User user = getUserBuyID(from_user);
		      
		      String like_user = user.fullname;
		      String title =  product.title;
		      int object_type = 1;
		      
		      String content = "$1 đã bán sản phẩm \"$2\".";
		      
		      content = content.replace("$1", like_user);
		      content = content.replace("$2", title);
		      
		      // Send notify to user follower
		      List<Integer> listUserFollower = getUserFollowerSendNotify(from_user);
		      String strUserId = "";
		   
		     for (Integer userID : listUserFollower) {
		    	 strUserId =+ userID+",";
		    	 saveNotify(product_id, userID, from_user, content, "", "", object_type, like_user);
			  } 
			 
		      if(strUserId.length()>0)
		    	  strUserId = strUserId.substring(0,strUserId.length()-1);
		      
		      // Send notify to user like product
		      List<Integer> arrUserFollower = getUserLikeProduct(product_id,strUserId);
		      for(Integer userID : arrUserFollower){
		          saveNotify(product_id, userID, from_user, content, "", "", object_type, like_user);
		      }
		    
		      // Send notify tới chủ shop
		      User userBuyer = getUserBuyID(buyer_id);
		      content =  "Sản phẩm: '$1' của bạn, có 1 giao dịch";
		      content = content.replace("$1",title);
		      saveNotify(product_id, from_user, buyer_id, content, "", "", object_type, userBuyer.fullname);
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	public int checkNotify(int object_id,int type,int to_user,int from_user) {
		PreparedStatement ps=null;
		Connection connection=null;
		ResultSet rs = null;
		
		int kq = 0;
		try {
			connection = C3p0SwapHubPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM ms_notify  WHERE   object_id = ? And object_type= ?  And  to_user = ? And from_user = ? ");
			ps.setInt(1, object_id);
			ps.setInt(2, type);
			ps.setInt(3, to_user);
			ps.setInt(4, from_user);
			rs = ps.executeQuery();
			
			if (rs.next()) {
				kq = rs.getInt("id");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return kq;
	}
	
	public int saveOffer(Offer offer) {
		PreparedStatement ps=null;
		Connection connection=null;
		
		int id = 0;
		try {
			connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("INSERT INTO ms_offer "
					+ "(seller_id, buyer_id,product_id,product_swap_ids,price,message_type,quantity,create_date,update_date)"
					+ "VALUES (?,?,?,?,?,?,?,NOW(),NOW())");
			ps.setInt(1, offer.seller_id);
			ps.setInt(2, offer.buyer_id);
			ps.setInt(3, offer.product_id);
			ps.setString(4, offer.product_swap_ids);
			ps.setDouble(5, offer.price);
			ps.setInt(6, offer.message_type.getByteValue());
			ps.setInt(7, offer.quantity);
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			String sql = "SELECT LAST_INSERT_ID()";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				id = resultSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return id;
	}
	
	public int updateOffer(Offer offer) {
		PreparedStatement ps=null;
		Connection connection=null;
		int id = 0;
		try {
			connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("Update  ms_offer "
					+ "SET product_swap_ids=? , price = ? ,message_type = ?,quantity = ?,update_date = NOW() Where id = ? ");
			ps.setString(1, offer.product_swap_ids);
			ps.setDouble(2, offer.price);
			ps.setInt(3, offer.message_type.getByteValue());
			ps.setInt(4, offer.quantity);
			ps.setInt(5, offer.id);
			logger.info("Cap nhat type message:"+offer.message_type.getByteValue());
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			
		} catch (SQLException e) {
			logger.debug("updateOffer:"+e.getMessage());
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return id;
	}
	
	public int updateOffer(int id,SwapType message_type) {
		PreparedStatement ps=null;
		Connection connection=null;
		try {
			connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("Update  ms_offer "
					+ "SET message_type = ?, update_date = NOW() Where id = ? ");
			ps.setInt(1, message_type.getByteValue());
			logger.info("Cap nhat type message:"+message_type.getByteValue());
			ps.setInt(2, id);
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			
		} catch (SQLException e) {
			logger.debug("updateOffer:"+e.getMessage());
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return id;
	}
	
	public Offer getOffer(int seller_id, int buyer_id,int product_id) {
		PreparedStatement ps=null;
		Connection connection=null;
		ResultSet rs = null;
		Offer offer = null;
		
		try {
			connection = C3p0SwapHubPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM ms_offer  WHERE   seller_id = ? AND buyer_id = ? "
					+ "AND product_id = ? ");
			ps.setInt(1, seller_id);
			ps.setInt(2, buyer_id);
			ps.setInt(3, product_id);
			rs = ps.executeQuery();
			if (rs.next()) {
				offer = new Offer();
				offer.seller_id = rs.getInt("seller_id");
				offer.buyer_id = rs.getInt("buyer_id");
				offer.product_id = rs.getInt("product_id");
				int type = rs.getInt("message_type");
				offer.message_type = SwapType.fromByte((byte)type);
				offer.price = rs.getDouble("price");
				offer.quantity  = rs.getInt("quantity");
				offer.product_swap_ids = rs.getString("product_swap_ids");
				offer.create_date = rs.getString("create_date");
				offer.update_date = rs.getString("update_date");
				offer.id = rs.getInt("id");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return offer;
	}
	
	public Offer getOfferChangeTypeMessage(int seller_id, int buyer_id,int product_id) {
		PreparedStatement ps=null;
		Connection connection=null;
		ResultSet rs = null;
		Offer offer = null;
		
		try {
			connection = C3p0SwapHubPool.getConnection();
			ps = connection.prepareStatement("SELECT * FROM ms_offer  WHERE   (seller_id = ? OR seller_id = ? ) AND (buyer_id = ? OR buyer_id = ? ) "
					+ "AND product_id = ? ");
			ps.setInt(1, seller_id);
			ps.setInt(2, buyer_id);
			ps.setInt(3, seller_id);
			ps.setInt(4, buyer_id);
			ps.setInt(5, product_id);
			rs = ps.executeQuery();
			if (rs.next()) {
				offer = new Offer();
				offer.seller_id = rs.getInt("seller_id");
				offer.buyer_id = rs.getInt("buyer_id");
				offer.product_id = rs.getInt("product_id");
				int type = rs.getInt("message_type");
				offer.message_type = SwapType.fromByte((byte)type);
				offer.price = rs.getDouble("price");
				offer.quantity  = rs.getInt("quantity");
				offer.product_swap_ids = rs.getString("product_swap_ids");
				offer.create_date = rs.getString("create_date");
				offer.update_date = rs.getString("update_date");
				offer.id = rs.getInt("id");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(rs);
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return offer;
	}
	
	
	public int subQuantityAndOfferProduct(int product_id,String offer) {
		PreparedStatement ps=null;
		Connection connection=null;
		int kq= 0;
		try {
			connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE ms_product  SET quantity = 0, offer = ?,update_date=NOW()  WHERE id = ?");
			ps.setString(1, offer);
			ps.setInt(2, product_id);
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			kq= 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return kq;
	}
	
	public int addQuantityAndOfferProduct(int product_id,String offer) {
		PreparedStatement ps=null;
		Connection connection=null;
		int kq= 0;
		try {
			connection = C3p0SwapHubPool.getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement("UPDATE ms_product SET quantity = 1,offer = ?,update_date=NOW() WHERE id = ?");
			ps.setString(1, offer);
			ps.setInt(2, product_id);
			ps.execute();
			connection.commit();
			connection.setAutoCommit(true);
			kq= 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			C3p0SwapHubPool.attemptClose(ps);
			C3p0SwapHubPool.attemptClose(connection);
		}
		return kq;
	}
	
	public static void main(String[] args) {
		int type = 11;
		String strUserId = "1,2,";
		 strUserId = strUserId.substring(0,strUserId.length()-1);
		 System.out.println(strUserId);
		System.out.println(SwapType.fromByte((byte)type));
		System.out.println((byte)type);
	}
}