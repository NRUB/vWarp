package vwarp.system;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import vwarp.VWarp;

/**
 *
 * @author NRUB
 */
public class Warps implements Comparable<Warps> {

    private final String nick;
    private LinkedList<Integer> numbers;

    public Warps(String nick, LinkedList numbers) {
        super();
        this.nick = nick;
        this.numbers = numbers;
    }

    public String getNick() {
        return nick;
    }

    public LinkedList getNumbers() {
        return numbers;
    }

    public void setNumbers(LinkedList num) {
        numbers = num;
    }

    public void addNumbers(int num) {
        numbers.add(num);
    }

    @Override
    public String toString() {

        Collections.sort(numbers);

        String player = Lang.getMessage("NC");
        player += nick + " " + Lang.getMessage("LC") + "[";
        Iterator<Integer> i = numbers.iterator();
        while (i.hasNext()) {
            Warp last;
            int number = i.next();
            int x = 0;
            do {
                last = VWarp.getWarpList().get(x);
                if (last.getName().equalsIgnoreCase(nick) && last.getNumber() == number) {
                    x = VWarp.getWarpList().size();
                }
                x++;
            } while (x < VWarp.getWarpList().size());
            if (CheckSafety.insecure(last)) {
                player += (Lang.getMessage("IC"));
            }
            player += number;
            player += Lang.getMessage("LC");
            if (i.hasNext()) {
                player += ", ";
            }
        }
        player += "]";
        return player;
    }

    @Override
    public int compareTo(Warps w1) {
        return nick.toLowerCase().compareTo(w1.nick.toLowerCase());
    }
}
