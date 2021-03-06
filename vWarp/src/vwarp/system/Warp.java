package vwarp.system;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 *
 * @author NRUB
 */
public class Warp {

    private String vwarpName;
    private int vwarpNumber;
    private double X;
    private double Y;
    private double Z;
    private float yaw;
    private float pitch;
    private String world;

    public Warp(String name, int number, double X, double Y, double Z, float yaw, float pitch, String world) { //constructor
        vwarpName = name;
        vwarpNumber = number;
        this.X = (double) (((int) X) - 0.5);
        if (this.X > 0) {
            this.X++;
        }
        this.Y = Y;
        this.Z = (double) (((int) Z) - 0.5);
        if (this.Z > 0) {
            this.Z++;
        }
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    public Warp() { //default constructor
    }

    public String getName() {
        return vwarpName;
    }

    public void setName(String name) {
        vwarpName = name;
    }

    public int getNumber() {
        return vwarpNumber;
    }

    public void setNumber(int number) {
        vwarpNumber = number;
    }

    public double getX() {
        return X;
    }

    public void setX(double X) {
        int temp = (int) X;
        this.X = (double) temp + 0.5;
    }

    public double getY() {
        return Y;
    }

    public void setY(double Y) {
        int temp = (int) Y;
        this.Y = (double) temp + 0.5;
    }

    public double getZ() {
        return Z;
    }

    public void setZ(double Z) {
        int temp = (int) Z;
        this.Z = (double) temp + 0.5;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), X, Y, Z, yaw, pitch);
    }

    @Override
    public String toString() { //parsing object Warp to String
        StringBuilder line = new StringBuilder(64);

        line.append(vwarpName);
        line.append(";");
        line.append(vwarpNumber);
        line.append(";");
        line.append(X);
        line.append(";");
        line.append(Y);
        line.append(";");
        line.append(Z);
        line.append(";");
        line.append(yaw);
        line.append(";");
        line.append(pitch);
        line.append(";");
        line.append(world);
        line.append(";\n");

        return line.toString();
    }
}
