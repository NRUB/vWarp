package vwarp.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author NRUB
 */
public class Lang {

    private static String NICP; //NOT_IN_COMMAND_PROMPT
    private static String CUICP; //CORRECT_USE_IN_COMMAND_PROMPT
    private static String TBNOvwarp; //TOO_BIG_NUMBER_OF_VWARP
    private static String BW; //BAD_WORLD

    private static String WOTTvwarp; //WAIT_ON_TELEPORTATION_TO_VWARP
    private static String TTvwarp; //TELEPORTED_TO_VWARP
    private static String ATRTvwarp; //ABORTED_TELEPORT_REQUEST_TO_VWARP
    private static String NE; //NOT_EXIST
    private static String SVID; //SELECTED_VWARP_IS_DANGEROUS
    private static String SVMBNS; //SELECTED_VWARP_MIGHT_BE_NOT_SAFETY

    private static String CDvwarp; //CREATE_DEFAULT_VWARP
    private static String CEvwarp; //CREATE_EXTRA_VWARP

    private static String DDvwarp; //DELETE_DEFAULT_VWARP
    private static String DEvwarpF; //DELETE_EXTRA_VWARP_FRONT
    private static String DEvwarpE; //DELETE_EXTRA_VWARP_END

    private static String ACDvwarp; //ADMIN_CREATE_DEFAULT_VWARP
    private static String ACEvwarpF; //ADMIN_CREATE_EXTRA_VWARP_FRONT
    private static String ACEvwarpE; //ADMIN_CREATE_EXTRA_VWARP_END

    private static String ADDvwarp; //ADMIN_DELETE_DEFAULT_VWARP
    private static String ADEvwarpF; //ADMIN_DELETE_EXTRA_VWARP_FRONT
    private static String ADEvwarpE; //ADMIN_DELETE_EXTRA_VWARP_END

    private static String NC; //NICK_COLOR
    private static String LC; //LIST_COLOR
    private static String IC; //INSECURE_COLOR
    private static String POL; //PAGE_OF_LIST
    private static String CUV; //CORRECT_USAGE_VWARPS

    private static String RP; //RELOAD_PLUGIN
    private static String RI; //RELOADING_INTERRUPTED
    private static String BP; //BACKUP_PLUGIN
    private static String BI; //BACKUP_INTERRUPTED
    private static String NB; //NO_BACKUPS
    private static String SBL; //SELECTED_BACKUP_LOADED
    private static String SBNF; //SELECTED_BACKUP_NOT_FOUND
    private static String SBI; //SELECTED_BACKUP_INTERRUPTED
    private static String DLE; //DATABASE_LOAD_ERROR

    private static Map<String, String> langPack;

    public static void load(JavaPlugin plugin) throws IOException {
        Yaml lang = new Yaml();
        InputStream loadLang;
        try {
            loadLang = new FileInputStream(new File("plugins/vWarp/lang.yml"));
        } catch (FileNotFoundException FNFex) {
            InputStream in = plugin.getResource("lang.yml");
            OutputStream out = new FileOutputStream(new File("plugins/vWarp", "lang.yml"));
            byte[] buf = new byte[2048];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            loadLang = new FileInputStream(new File("plugins/vWarp/lang.yml"));
        }
        langPack = (Map<String, String>) lang.load(loadLang);

        NICP = langPack.get("NOT_IN_COMMAND_PROMPT");
        CUICP = langPack.get("CORRECT_USE_IN_COMMAND_PROMPT");
        TBNOvwarp = langPack.get("TOO_BIG_NUMBER_OF_VWARP");
        BW = langPack.get("BAD_WORLD");

        WOTTvwarp = langPack.get("WAIT_ON_TELEPORTATION_TO_VWARP");
        ATRTvwarp = langPack.get("ABORTED_TELEPORT_REQUEST_TO_VWARP");
        TTvwarp = langPack.get("TELEPORTED_TO_VWARP");
        NE = langPack.get("NOT_EXIST");
        SVID = langPack.get("SELECTED_VWARP_IS_DANGEROUS");
        SVMBNS = langPack.get("SELECTED_VWARP_MIGHT_BE_NOT_SAFETY");

        CDvwarp = langPack.get("CREATE_DEFAULT_VWARP");
        CEvwarp = langPack.get("CREATE_EXTRA_VWARP");

        DDvwarp = langPack.get("DELETE_DEFAULT_VWARP");
        DEvwarpF = langPack.get("DELETE_EXTRA_VWARP_FRONT");
        DEvwarpE = langPack.get("DELETE_EXTRA_VWARP_END");

        ACDvwarp = langPack.get("ADMIN_CREATE_DEFAULT_VWARP");
        ACEvwarpF = langPack.get("ADMIN_CREATE_EXTRA_VWARP_FRONT");
        ACEvwarpE = langPack.get("ADMIN_CREATE_EXTRA_VWARP_END");

        ADDvwarp = langPack.get("ADMIN_DELETE_DEFAULT_VWARP");
        ADEvwarpF = langPack.get("ADMIN_DELETE_EXTRA_VWARP_FRONT");
        ADEvwarpE = langPack.get("ADMIN_DELETE_EXTRA_VWARP_END");

        RP = langPack.get("RELOAD_PLUGIN");
        RI = langPack.get("RELOADING_INTERRUPTED");
        BP = langPack.get("BACKUP_PLUGIN");
        BI = langPack.get("BACKUP_INTERRUPTED");
        NB = langPack.get("NO_BACKUPS");
        SBL = langPack.get("SELECTED_BACKUP_LOADED");
        SBNF = langPack.get("SELECTED_BACKUP_NOT_FOUND");
        SBI = langPack.get("SELECTED_BACKUP_INTERRUPTED");
        DLE = langPack.get("DATABASE_LOAD_ERROR");

        NC = langPack.get("NICK_COLOR");
        LC = langPack.get("LIST_COLOR");
        IC = langPack.get("INSECURE_COLOR");
        POL = langPack.get("PAGE_OF_LIST");
        CUV = langPack.get("CORRECT_USAGE_VWARPS");
    }

    public static String getMessage(String msg) {
        switch (msg) {
            case "NICP":
                return NICP;
            case "CUICP":
                return CUICP;
            case "TBNOvwarp":
                return TBNOvwarp;
            case "BW":
                return BW;

            case "WOTTvwarp":
                return WOTTvwarp;
            case "ATRTvwarp":
                return ATRTvwarp;
            case "TTvwarp":
                return TTvwarp;
            case "NE":
                return NE;
            case "SVID":
                return SVID;
            case "SVMBNS":
                return SVMBNS;

            case "CDvwarp":
                return CDvwarp;
            case "CEvwarp":
                return CEvwarp;

            case "DDvwarp":
                return DDvwarp;
            case "DEvwarpF":
                return DEvwarpF;
            case "DEvwarpE":
                return DEvwarpE;

            case "ACDvwarp":
                return ACDvwarp;
            case "ACEvwarpF":
                return ACEvwarpF;
            case "ACEvwarpE":
                return ACEvwarpE;

            case "ADDvwarp":
                return ADDvwarp;
            case "ADEvwarpF":
                return ADEvwarpF;
            case "ADEvwarpE":
                return ADEvwarpE;

            case "RP":
                return RP;
            case "RI":
                return RI;
            case "BP":
                return BP;
            case "BI":
                return BI;
            case "NB":
                return NB;
            case "SBL":
                return SBL;
            case "SBNF":
                return SBNF;
            case "SBI":
                return SBI;
            case "DLE":
                return DLE;

            case "NC":
                return NC;
            case "LC":
                return LC;
            case "IC":
                return IC;
            case "POL":
                return POL;
            case "CUV":
                return CUV;
        }
        return null;
    }
}
