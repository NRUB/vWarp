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
    private static String TBNOV; //TOO_BIG_NUMBER_OF_VWARP
    private static String BW; //BAD_WORLD

    private static String WOTTV; //WAIT_ON_TELEPORTATION_TO_VWARP
    private static String TTV; //TELEPORTED_TO_VWARP
    private static String ATRTV; //ABORTED_TELEPORT_REQUEST_TO_VWARP
    private static String NE; //NOT_EXIST
    private static String SVID; //SELECTED_VWARP_IS_DANGEROUS
    private static String SVMNBS; //SELECTED_VWARP_MIGHT_NOT_BE_SAFE

    private static String CDV; //CREATE_DEFAULT_VWARP
    private static String CEV; //CREATE_EXTRA_VWARP

    private static String DDV; //DELETE_DEFAULT_VWARP
    private static String DEV; //DELETE_EXTRA_VWARP

    private static String ACDVA; //ADMIN_CREATE_DEFAULT_VWARP_A
    private static String ACDVB; //ADMIN_CREATE_DEFAULT_VWARP_B
    private static String ACEVA; //ADMIN_CREATE_EXTRA_VWARP_A
    private static String ACEVB; //ADMIN_CREATE_EXTRA_VWARP_B

    private static String ADDVA; //ADMIN_DELETE_DEFAULT_A
    private static String ADDVB; //ADMIN_DELETE_DEFAULT_B
    private static String ADEVA; //ADMIN_DELETE_EXTRA_VWARP_A
    private static String ADEVB; //ADMIN_DELETE_EXTRA_VWARP_B

    private static String RP; //RELOAD_PLUGIN
    private static String RI; //RELOADING_INTERRUPTED
    private static String BP; //BACKUP_PLUGIN
    private static String BI; //BACKUP_INTERRUPTED
    private static String NB; //NO_BACKUPS
    private static String SBL; //SELECTED_BACKUP_LOADED
    private static String SBNF; //SELECTED_BACKUP_NOT_FOUND
    private static String SBI; //SELECTED_BACKUP_INTERRUPTED
    private static String DLE; //DATABASE_LOAD_ERROR

    private static String NC; //NICK_COLOR
    private static String LC; //LIST_COLOR
    private static String IC; //INSECURE_COLOR
    private static String POL; //PAGE_OF_LIST
    private static String CUV; //CORRECT_USAGE_VWARPS

    private static String UAA; //UPDATE_ANNOUNCEMENT_A
    private static String UAB; //UPDATE_ANNOUNCEMENT_B
    private static String UAC; //UPDATE_ANNOUNCEMENT_C

    private static Map<String, String> langPack;

    public static void load(JavaPlugin plugin) throws IOException {
        Yaml lang = new Yaml();
        InputStream loadLang;
        try {
            loadLang = new FileInputStream(new File("plugins/vWarp/lang.yml"));
        } catch (FileNotFoundException FNFex) {
            try (InputStream in = plugin.getResource("lang.yml"); OutputStream out = new FileOutputStream(new File("plugins/vWarp", "lang.yml"))) {
                byte[] buf = new byte[2048];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            loadLang = new FileInputStream(new File("plugins/vWarp/lang.yml"));
        }
        langPack = (Map<String, String>) lang.load(loadLang);

        NICP = langPack.get("NOT_IN_COMMAND_PROMPT");
        TBNOV = langPack.get("TOO_BIG_NUMBER_OF_VWARP");
        BW = langPack.get("BAD_WORLD");

        WOTTV = langPack.get("WAIT_ON_TELEPORTATION_TO_VWARP");
        TTV = langPack.get("TELEPORTED_TO_VWARP");
        ATRTV = langPack.get("ABORTED_TELEPORT_REQUEST_TO_VWARP");
        NE = langPack.get("NOT_EXIST");
        SVID = langPack.get("SELECTED_VWARP_IS_DANGEROUS");
        SVMNBS = langPack.get("SELECTED_VWARP_MIGHT_NOT_BE_SAFE");

        CDV = langPack.get("CREATE_DEFAULT_VWARP");
        CEV = langPack.get("CREATE_EXTRA_VWARP");

        DDV = langPack.get("DELETE_DEFAULT_VWARP");
        DEV = langPack.get("DELETE_EXTRA_VWARP");

        ACDVA = langPack.get("ADMIN_CREATE_DEFAULT_VWARP_A");
        ACDVB = langPack.get("ADMIN_CREATE_DEFAULT_VWARP_B");
        ACEVA = langPack.get("ADMIN_CREATE_EXTRA_VWARP_A");
        ACEVB = langPack.get("ADMIN_CREATE_EXTRA_VWARP_B");

        ADDVA = langPack.get("ADMIN_DELETE_DEFAULT_VWARP_A");
        ADDVB = langPack.get("ADMIN_DELETE_DEFAULT_VWARP_B");
        ADEVA = langPack.get("ADMIN_DELETE_EXTRA_VWARP_A");
        ADEVB = langPack.get("ADMIN_DELETE_EXTRA_VWARP_B");

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

        UAA = langPack.get("UPDATE_ANNOUNCEMENT_A");
        UAB = langPack.get("UPDATE_ANNOUNCEMENT_B");
        UAC = langPack.get("UPDATE_ANNOUNCEMENT_C");
    }

    public static String getMessage(Messages msg) {
        switch (msg) {
            case NICP:
                return NICP;
            case TBNOV:
                return TBNOV;
            case BW:
                return BW;

            case WOTTV:
                return WOTTV;
            case ATRTV:
                return ATRTV;
            case TTV:
                return TTV;
            case NE:
                return NE;
            case SVID:
                return SVID;
            case SVMNBS:
                return SVMNBS;

            case CDV:
                return CDV;
            case CEV:
                return CEV;

            case DDV:
                return DDV;
            case DEV:
                return DEV;

            case ACDVA:
                return ACDVA;
            case ACDVB:
                return ACDVB;
            case ACEVA:
                return ACEVA;
            case ACEVB:
                return ACEVB;

            case ADDVA:
                return ADDVA;
            case ADDVB:
                return ADDVB;
            case ADEVA:
                return ADEVA;
            case ADEVB:
                return ADEVB;

            case RP:
                return RP;
            case RI:
                return RI;
            case BP:
                return BP;
            case BI:
                return BI;
            case NB:
                return NB;
            case SBL:
                return SBL;
            case SBNF:
                return SBNF;
            case SBI:
                return SBI;
            case DLE:
                return DLE;

            case NC:
                return NC;
            case LC:
                return LC;
            case IC:
                return IC;
            case POL:
                return POL;
            case CUV:
                return CUV;

            case UAA:
                return UAA;
            case UAB:
                return UAB;
            case UAC:
                return UAC;
        }
        return null;
    }
}
