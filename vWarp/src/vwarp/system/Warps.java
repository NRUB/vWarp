package vwarp.system;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import vwarp.VWarp;

/**
 *
 * @author NRUB
 */
public class Warps implements Comparable<Warps> {

    private final String nick;
    private List<Integer> numbers;

    public Warps(String nick, List<Integer> numbers) {
        super();
        this.nick = nick;
        this.numbers = numbers;
    }

    public String getNick() {
        return nick;
    }

    public List getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> num) {
        numbers = num;
    }

    public void addNumbers(int num) {
        numbers.add(num);
    }

    @Override
    public String toString() {

        Collections.sort(numbers);

        StringBuilder player = new StringBuilder(32);
        player.append(Lang.getMessage(Messages.NC));
        player.append(nick);
        player.append(" ");
        player.append(Lang.getMessage(Messages.LC));
        player.append("[");
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
                player.append(Lang.getMessage(Messages.IC));
            }
            player.append(number);
            player.append(Lang.getMessage(Messages.LC));
            if (i.hasNext()) {
                player.append(", ");
            }
        }
        player.append("]");
        return player.toString();
    }

    @Override
    public int compareTo(Warps w1) {
        return nick.toLowerCase().compareTo(w1.nick.toLowerCase());
    }
}
