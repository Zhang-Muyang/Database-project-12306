import java.sql.Time;
import java.util.List;

public interface AdministratorInterface {
    public Administrator checkAd(String user_name, String password);
    public String checkTrainId(String id);
    public void deleteTrain(String id);
    public void deleteLine(String id);
    public void deleteOrderD(String id);
    public void deleteOrderA(String id);
    public void deleteRe(String id);
    public void deleteReByID(int stop);
    public void deleteStop(int nth,String tr_name);


    public String checkStatId(String id);
    public void deleteStation(String st);
    public void deleteLineSt(int id);
    public void InsertStat(String id,String id2);
    public void InsertStop(String train_name,int nth,int station,
                           Time artime, Time detime, int days, int miles);
    public void InsertTrain(String train_name, int departure, int arrival,
                            Time detime,Time artime, int days, int miles);
    public List<String> SelTrain(int st);
    public int getID(String n);
    public int getStopId(String tr_name, int nth);

    public int getMiles(String tr_name, int nth);



    public int selMaxID();
    public void InsertRem(int id, String gotime);
    public void UpdateStation(String old, String n);
    public int getStID(String station);
    public int getNum(String train);
    public List<String> getSts(String train);
    public void UpdateInfoSt(String name_,int nth,String tr_name);
    public void UpdateTrDe(String t_name,String de_name);
    public void UpdateTrAr(String t_name,String ar_name);
    public List<Line> selLineInfo(String t_name);
    public void UpdateInfoDe(Time time,int nth, String tr_name);
    public void UpdateInfoAr(Time time,int nth, String tr_name);
    public void UpdateTrDeTi(String t_name,Time time);
    public void UpdateTrArTi(String t_name,Time time);
    public void Updateth(String t_name, int nth);
    public void Updateth2(String t_name, int nth);
}