package syun.github.minecraft.plugin.clicktnt.clicktnt;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Random;

public final class ClickTNT extends JavaPlugin implements Listener {

    // 発射したTNTが爆発するまでの時間
    private static final int TNT_TICK = 40;
    // TNTが飛ぶスピード
    private static final double TNT_SPEED = 1.5;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("クリックTNTプラグインが起動しました！");
        // イベント登録（クリックしたときみたいなイベントを登録しないと、クリックした時に反応してくれない）
        getServer().getPluginManager().registerEvents(this,this);
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("クリックTNTプラグインが終了しました！");
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        // プレイヤーが行ったアクション（行動）の情報を取得
        Action action = e.getAction();
        // （空中を左クリック or ブロックを左クリック）and メインハンドでクリックしたとき（マイクラの仕様上メインハンドとオフハンドそれぞれでクリック判定が出るためこれをしないと２回実行される）
        if((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && e.getHand() == EquipmentSlot.HAND){
            // プレイヤー情報を取得
            Player player = e.getPlayer();
            // プレイヤー情報の中のプレイヤーの位置情報を取得
            Location loc = player.getLocation();
            // world.spawnEntity()でTNTを生成（ワールドを複数同時に稼働させることがあるためworldのメソッドになっている）
            TNTPrimed tnt1 =  (TNTPrimed) loc.getWorld().spawnEntity(player.getEyeLocation(), EntityType.PRIMED_TNT);
            TNTPrimed tnt2 =  (TNTPrimed) loc.getWorld().spawnEntity(player.getEyeLocation(), EntityType.PRIMED_TNT);
            TNTPrimed tnt3 =  (TNTPrimed) loc.getWorld().spawnEntity(player.getEyeLocation(), EntityType.PRIMED_TNT);
            Random random = new Random();
            tnt1.setFuseTicks(TNT_TICK + random.nextInt(20));
            tnt2.setFuseTicks(TNT_TICK + random.nextInt(20));
            tnt3.setFuseTicks(TNT_TICK + random.nextInt(20));
            Vector vec1 = player.getLocation().getDirection().clone();
            Vector vec2 = player.getLocation().getDirection().clone();
            Vector vec3 = player.getLocation().getDirection().clone();
            tnt1.setVelocity(vec1.clone().add(Vector.getRandom().add(new Vector(-0.5,-0.5,-0.5)).multiply(0.1)).normalize().multiply(TNT_SPEED));
            tnt2.setVelocity(vec2.clone().add(Vector.getRandom().add(new Vector(-0.5,-0.5,-0.5)).multiply(0.1)).normalize().multiply(TNT_SPEED));
            tnt3.setVelocity(vec3.clone().add(Vector.getRandom().add(new Vector(-0.5,-0.5,-0.5)).multiply(0.1)).normalize().multiply(TNT_SPEED));

            loc.getWorld().playSound(loc, Sound.ENTITY_CHICKEN_DEATH,1,1);
            for(int i = 0; i < TNT_TICK;i++){
                getServer().getScheduler().runTaskLater(this, new Runnable() {
                    @Override
                    public void run() {
                        tnt1.getLocation().getWorld().spawnParticle(Particle.END_ROD, tnt1.getLocation(), 10,0,0,0,0);
                        tnt2.getLocation().getWorld().spawnParticle(Particle.END_ROD, tnt2.getLocation(), 10,0,0,0,0);
                        tnt3.getLocation().getWorld().spawnParticle(Particle.END_ROD, tnt3.getLocation(), 10,0,0,0,0);
                    }
                },i);
            }
        }
    }
}
