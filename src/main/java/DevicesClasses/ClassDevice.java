package DevicesClasses;
/**
 * Класс устройств.
 */
public class ClassDevice {

    /**
     * Перечисление статусов подключения.
     */
    public enum EnumLink{
        Unknown,
        LinkNo,
        LinkYes,
        LinkConnect
    }

    /**
     * Перечисление протоколов подключения.
     */
    public enum EnumProtocol
    {
        RTU(1),
        TCP(2),
        SMS(3),
        GPRS(4),
        GPRS_SMS(5);

        EnumProtocol(int i){
        }
    }

    /** <summary>
    * Перечисление типов устройств.
    */
    public enum  EnumModel{
        None,
        BKM_3,
        BKM_4,
        SKZ,
        SKZ_IP,
        BSZ,
        USIKP,
        BKM_5,
        KIP
    }

    private String _Name;
    private int _Address;
    private EnumProtocol _Protocol;
    private int _Period;
    private String _IPAddress;
    private int _IPPort;
    private EnumLink _LinkState;
    private int _TxCounter;
    private int _RxCounter;
    private int _PacketLost;
   // private DateTime _DTAct;
    private String _ComPort;
    private String _SIM;
    private EnumModel _Model;
    //private DateTime _DTConnect;
    private Boolean _WaitAnswer;
    private String _Picket;
    private double _Latitude =  00.000000D;
    private double _Longitude = 00.000000D;
    private double _Elevation;
}
