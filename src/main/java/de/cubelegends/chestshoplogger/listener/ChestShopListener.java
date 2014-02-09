package de.cubelegends.chestshoplogger.listener;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.Acrobot.Breeze.Utils.PriceUtil;
import com.Acrobot.ChestShop.Events.ShopCreatedEvent;
import com.Acrobot.ChestShop.Events.ShopDestroyedEvent;
import com.Acrobot.ChestShop.Events.TransactionEvent;

import de.cubelegends.chestshoplogger.ChestShopLogger;
import de.cubelegends.chestshoplogger.handler.DBHandler;

public class ChestShopListener implements Listener {
	
	private ChestShopLogger plugin;
	private DBHandler db;
	
	public ChestShopListener(ChestShopLogger plugin) {
		this.plugin = plugin;
		this.db = plugin.getDBHandler();
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onShopCreate(ShopCreatedEvent e) {
		
		int coordX = e.getChest().getX();
		int coordY = e.getChest().getY();
		int coordZ = e.getChest().getZ();
		String world = e.getChest().getWorld().getName();
		String player = e.getSignLine((short) 0);
		String playerUID = "";
		if(plugin.getServer().getPlayer(player) != null) {
			playerUID = plugin.getServer().getPlayer(player).getUniqueId().toString();
		}
		int amount = Integer.parseInt(e.getSignLine((short) 1));
		double buyPrice = PriceUtil.getBuyPrice(e.getSignLine((short) 2));
		double sellPrice = PriceUtil.getSellPrice(e.getSignLine((short) 2));
		String material = e.getSignLine((short) 3);
		
		try {
			PreparedStatement ps = db.getConnection().prepareStatement("INSERT INTO chestshop_shop (coordx, coordy, coordz, world, player, playeruid, amount, buyprice, sellprice, material) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setInt(1, coordX);
			ps.setInt(2, coordY);
			ps.setInt(3, coordZ);
			ps.setString(4, world);
			ps.setString(5, player);
			ps.setString(6, playerUID);
			ps.setInt(7, amount);
			ps.setDouble(8, buyPrice);
			ps.setDouble(9, sellPrice);
			ps.setString(10, material);
			ps.execute();
			ps.close();
			db.closeConnection();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onShopDestroy(ShopDestroyedEvent e) {
		
		int coordX = e.getChest().getX();
		int coordY = e.getChest().getY();
		int coordZ = e.getChest().getZ();
		
		try {
			PreparedStatement ps = db.getConnection().prepareStatement("DELETE FROM chestshop_shop WHERE coordx = ? AND coordy = ? AND coordz = ?");
			ps.setInt(1, coordX);
			ps.setInt(2, coordY);
			ps.setInt(3, coordZ);
			ps.execute();
			ps.close();
			db.closeConnection();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onTransaction(TransactionEvent e) {
		
	}

}
