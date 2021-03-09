import org.apache.ibatis.session.SqlSession;


import java.sql.SQLOutput;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Administrator {
    Integer user_id;
    String user_name;
    String user_password;
   // String phone_num;

    //String id_card_num;
    final static String[]  dates= {"2020-5-21","2020-5-22","2020-5-23","2020-5-24",
            "2020-5-25","2020-5-26","2020-5-27","2020-5-28","2020-5-29",
            "2020-5-30","2020-5-31","2020-6-1","2020-6-2","2020-6-3",
            "2020-6-4","2020-6-5","2020-6-6","2020-6-7","2020-6-8",
            "2020-6-9","2020-6-10","2020-6-11","2020-6-12","2020-6-13",
            "2020-6-14","2020-6-15","2020-6-16","2020-6-17","2020-6-18",
            "2020-6-19"};
    Scanner scr = new Scanner(System.in);
    private AdministratorInterface adinterface;

    public Administrator(SqlSession sqlSession) {
        adinterface = sqlSession.getMapper(AdministratorInterface.class);
    }

    public Administrator() {
    }


    public Administrator logIn() {
        System.out.println("管理员登陆");
        System.out.println("请输入您的用户名");
        String user_name = scr.next();
        System.out.println("请输入您的密码");
        String user_password = scr.next();
        Administrator ad = adinterface.checkAd(user_name, user_password);

        if (ad == null) System.out.println("抱歉，您的用户名或密码输入错误");
        else {
            this.user_id = ad.user_id;
            this.user_name = ad.user_name;
            this.user_password = ad.user_password;
            System.out.println("登陆成功");
        }
        return ad;
    }

    public void DelTrain(String id) {


        String name = adinterface.checkTrainId(id);
        if(name != null){
            adinterface.deleteTrain(id);
            adinterface.deleteLine(id);
            adinterface.deleteOrderD(id);
            adinterface.deleteOrderA(id);
            adinterface.deleteRe(id);
            System.out.println("删除成功");
        }
        else System.out.println("该列车不存在");

    }

    public void DelStat() {
        System.out.println("请输入删除的车站名字：");
        String id = scr.next();

        String name = adinterface.checkStatId(id);
        if(name != null){
            int stId = adinterface.getStID(name);
            adinterface.deleteStation(id);

            List<String> list = adinterface.SelTrain(stId);

            for (String e: list) {
                DelTrain(e);
            }

            adinterface.deleteLineSt(stId);

            System.out.println("删除成功");
        }
        else System.out.println("该车站不存在");

    }

    public void AddStat() {
        System.out.println("请输入的车站名字：");
        String id = scr.next();

        String name = adinterface.checkStatId(id);
        if(name == null){
            System.out.println("请输入城市名字： ");
            String id2 = scr.next();

            adinterface.InsertStat(id,id2);
            System.out.println("添加成功");
        }
        else System.out.println("该车站已存在");
    }



    public void AddTrain() {
        System.out.println("请输入的列车名字：");
        String id = scr.next();

        String name = adinterface.checkTrainId(id);
        if(name == null){
            System.out.println("请输入途径车站数目（包括始发站和终点站）： ");
            int n = scr.nextInt();
            int[] stations = new int[n];
            Time[] detimes = new Time[n];
            Time[] artimes = new Time[n];
            int[] miles = new int[n];
            int[] days = new int[n];

            int currStop = adinterface.selMaxID()+1;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < 30; j++) {
                    adinterface.InsertRem(currStop,dates[j]);
                }
                currStop++;
            }

            for (int i = 0; i < n; i++) {
                System.out.println("请输入第"+(i+1)+"个车站名称：");
                String t = scr.next();   ///
                stations[i] = adinterface.getID(t);

                if(i == 0) {
                    artimes[i] = null;
                    miles[i] = 0;
                    days[i] = 1;
                }
                else {
                    System.out.println("请输入第"+(i+1)+"个车站到站时间：");
                    artimes[i] = Time.valueOf(scr.next());
                    System.out.println("请输入已行进公里数：");
                    miles[i] = scr.nextInt();
                    System.out.println("请输入已行进天数（不足一天按一天）：");
                    days[i] = scr.nextInt();
                }

                if(i == n-1) detimes[i] = null;
                else {
                    System.out.println("请输入第"+(i+1)+"个车站出站时间：");
                    detimes[i] = Time.valueOf(scr.next());

                }
            }

            for (int i = 0; i < n; i++) {
                adinterface.InsertStop(id,(i+1),stations[i],artimes[i],detimes[i],days[i],miles[i]);
            }

            adinterface.InsertTrain(id,stations[0],stations[n-1],detimes[0],artimes[n-1],days[n-1],miles[n-1]);




            System.out.println("添加成功");

        }
        else System.out.println("该车站已存在");
    }

    public void UpSt(){
        System.out.println("请输入修改的车站名称：");
        String name2 = scr.next();
        String name = adinterface.checkStatId(name2);
        if(name != null){
            System.out.println("请输入新的车站名称：");
            String t = scr.next();
            String t2 = adinterface.checkStatId(t);
            if(t2 == null){
                adinterface.UpdateStation(t,name2);
            }else System.out.println("新车站名与已有车站名重复");
        }else{
            System.out.println("该车站不存在");
        }
    }


    public void UpTr(){
        System.out.println("请输入即将修改的车次名称：");
        String name2 = scr.next();
        String name = adinterface.checkTrainId(name2);
        if(name != null){
            System.out.println("请输入是否修改途径车站：");
            String instr = scr.next();
            if(instr.equals("是")){
                int num = adinterface.getNum(name2);
                System.out.println("共有"+num+"站，如下：");
                List<String> Sts = adinterface.getSts(name2);
                int cnt = 1;
                for(String e: Sts){
                    System.out.print(cnt);
                    System.out.println("."+e);
                    cnt++;
                }

                System.out.println("请输入修改第几站：");
                int nth = scr.nextInt();
                System.out.println("请输入新的途径站名：");
                String name_ = scr.next();
                adinterface.UpdateInfoSt(name_,nth,name);
                if(nth==1){
                    adinterface.UpdateTrDe(name2,name_);
                }

                if(nth==num){
                    adinterface.UpdateTrAr(name2,name_);
                }
                System.out.println("修改成功");
            }

            System.out.println("请输入是否增加沿途站（除始发和终点）：");
            String instr4 = scr.next();
            if(instr4.equals("是")){
                int num = adinterface.getNum(name2);
                System.out.println("共有"+num+"站，如下：");
                List<Line> list = adinterface.selLineInfo(name2);
                int cnt = 1;
                Line[] l2 = new Line[num];
                for(Line e: list){
                    if(e.arrive_time == null) l2[0] = e;
                    else if(e.depart_time == null) l2[num-1] = e;
                    else l2[cnt++] = e;
                }
                for (int i = 0; i < num; i++) {
                    System.out.print(i+1);
                    System.out.println("."+l2[i].stationCity.station+"\t"+l2[i].arrive_time+"\t"
                            +l2[i].depart_time);
                }

                System.out.println("请输入在第几站后添加：");
                int nth = scr.nextInt();

                adinterface.Updateth(name2,nth);
                System.out.println("请输入车站名称：");
                String tmp = scr.next();
                int station = adinterface.getStID(tmp);

                System.out.println("请输入到站时间（注意时间关系）：");
                Time ar = Time.valueOf(scr.next());

                System.out.println("请输入出站时间（注意时间关系）：");
                Time de = Time.valueOf(scr.next());

                System.out.println();
                int a = adinterface.getMiles(name2,nth);
                System.out.println("已运行里程："+a);
                int b = adinterface.getMiles(name2,nth+2);
                System.out.println("下一站里程："+b);
                System.out.println("请输入里程（应在以上两里程之间）：");
                int ms = scr.nextInt();

                int tmpid = adinterface.selMaxID()+1;

                for (int j = 0; j < 30; j++) {
                    adinterface.InsertRem(tmpid,dates[j]);
                }

                adinterface.InsertStop(name2,nth+1,station,ar,de,1,ms);
            }


            System.out.println("请输入是否删减沿途站（除始发和终点）：");
            String instr5 = scr.next();
            if(instr5.equals("是")){
                int num = adinterface.getNum(name2);
                System.out.println("共有"+num+"站，如下：");
                List<String> Sts = adinterface.getSts(name2);
                int cnt = 1;
                for(String e: Sts){
                    System.out.print(cnt);
                    System.out.println("."+e);
                    cnt++;
                }

                System.out.println("请输入删除哪一站：");
                int nth = scr.nextInt();

                int si = adinterface.getStopId(name2,nth);

                adinterface.deleteReByID(si);
                adinterface.deleteStop(nth,name2);
                adinterface.Updateth2(name2,nth);
                System.out.println("修改成功");

            }


            System.out.println("请输入是否修改出站时间（除始发和终点）：");
            String instr2 = scr.next();

            if(instr2.equals("是")){
                int num = adinterface.getNum(name2);
                System.out.println("共有"+num+"站，如下：");
                List<Line> list = adinterface.selLineInfo(name2);
                int cnt = 1;
                Line[] l2 = new Line[num];
                for(Line e: list){
                    if(e.arrive_time == null) l2[0] = e;
                    else if(e.depart_time == null) l2[num-1] = e;
                    else l2[cnt++] = e;
                }
                for (int i = 0; i < num; i++) {
                    System.out.print(i+1);
                    System.out.println("."+l2[i].stationCity.station+"\t"+l2[i].arrive_time+"\t"
                            +l2[i].depart_time);
                }

                System.out.println("请输入修改第几站：");
                int nth = scr.nextInt();
                System.out.println("请输入该站新的出站时间：");
                Time time = Time.valueOf(scr.next());
                adinterface.UpdateInfoDe(time,nth,name2);

                if(nth == 1){
                    adinterface.UpdateTrDeTi(name2,time);
                }

                System.out.println("修改成功");

            }

            System.out.println("请输入是否修改入站时间：");
            String instr3 = scr.next();
            if(instr3.equals("是")){
                int num = adinterface.getNum(name2);
                System.out.println("共有"+num+"站，如下：");
                List<Line> list = adinterface.selLineInfo(name2);
                int cnt = 1;
                Line[] l2 = new Line[num];
                for(Line e: list){
                    if(e.arrive_time == null) l2[0] = e;
                    else if(e.depart_time == null) l2[num-1] = e;
                    else l2[cnt++] = e;
                }
                for (int i = 0; i < num; i++) {
                    System.out.print(i+1);
                    System.out.println("."+l2[i].stationCity.station+"\t"+l2[i].arrive_time
                            +"\t"+l2[i].depart_time);
                }

                System.out.println("请输入修改第几站：");
                int nth = scr.nextInt();
                System.out.println("请输入该站新的入站时间：");
                Time time = Time.valueOf(scr.next());
                adinterface.UpdateInfoAr(time,nth,name2);

                if(nth == num){
                    adinterface.UpdateTrArTi(name2,time);
                }

                System.out.println("修改成功");


            }
        }else{
            System.out.println("该车不存在");
        }


    }



}

