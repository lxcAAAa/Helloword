package FFFF;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
//import java.util.Calendar;
import java.io.*;
import java.io.IOException;

public class calendar extends JFrame
implements ActionListener,MouseListener
{
    int year,month,day;//年，月，日
    int yearafterquery,monthafterquery;
    int startday;
    String SwitchMonth;
    String key;
    int changeyearmessage;
    int changemonthmessage;
    int priormonth,prioryear;
    boolean ischange=false,ischange_priornext=false;
    private JPanel LeftPane,RightPane,jpanel;//面板容器
    
    private JLabel YearLabel;
    private JLabel MonthLabel;
    private JComboBox MonthCombobox;
    private JTextField ShowDays[]=new JTextField[42];
    private JTextField YearText;
    private JLabel Ask;
    private JLabel ShowDate;
    private JLabel Blank;
    private JLabel show;
    private  JLabel TopBarTitle[]=new JLabel[7];
    private JButton ToToday;
    private JButton Query;//查询
    private String week[]={"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
    
    private JLabel NorthMonthDayYear;
    private JTextArea CenterText;
    private JButton SouthSave,SouthDelete;
    private JButton PriorMonth;
    private JButton NextMonth;
    private ImageIcon background;
    //private JLabel  label;
   // private JFrame  frame;
    
   
	public calendar(int year,int month,int day)//构造方法
    {//准备
     setTitle("简易备忘录");
     RightPane=new JPanel();
     JPanel rightNorth=new JPanel();//面板北部
     JPanel rightCenter=new JPanel();//面板中心
     //向容器中添加组件
     RightPane.setLayout(new BorderLayout());//边框布局管理器
     RightPane.add(rightNorth,BorderLayout.NORTH);//添加进容器
     RightPane.add(rightCenter,BorderLayout.CENTER);//添加进容器
     RightPane.add(ToToday=new JButton("Go to today"),BorderLayout.SOUTH);//添加南部面板组件按钮
     ToToday.setBackground(Color.PINK);//设置按钮背景颜色
     ToToday.addActionListener(this);//添加动作事件监听器
     RightPane.validate();//固定面板
     
     //设置北部面板布局管理器
     rightNorth.setLayout(new GridLayout(3,1,0,0));//嵌套容器，网格布局，水平间距为0，垂直间距也为0
     rightNorth.add(Ask=new JLabel("请输入你要查询的日子"));//网格布局第一行
     JPanel North=new JPanel(new FlowLayout(0,8,0));//网格布局第2行
     rightNorth.add(North);//添加网格布局第2行
     North.add(YearLabel=new JLabel("年:"));
     YearLabel.setForeground(Color.pink);
     North.add(YearText=new JTextField(4));
     YearText.setBackground(Color.white);
     YearText.setForeground(Color.pink);
     YearText.setFont(new Font("TimesRoman",Font.BOLD,17));
     YearText.addActionListener(this);
     YearText.setFocusable(true);
     North.add(Blank=new JLabel("月:"));
     North.add(MonthCombobox=new JComboBox());
     Blank.setForeground(Color.pink);
     //
     for(int i=1;i<=12;i++)
     {
         MonthCombobox.addItem(i);//添加月份
     }
  
     
     MonthCombobox.setForeground(Color.pink);
     MonthCombobox.setFont(new Font("TimesRoman",Font.BOLD,12));
     North.add(Query=new JButton("查询"));
     Query.setForeground(Color.pink);
     Query.addActionListener(this);
     JPanel North2=new JPanel(new FlowLayout());//网格布局第三行
     rightNorth.add(North2);//添加第三行面板
     North2.add(PriorMonth=new JButton("上一月"));
     PriorMonth.addActionListener(this);
     PriorMonth.setActionCommand("prior");//设置一个字符串来判断你究竟选择的是哪一个组件
     PriorMonth.setForeground(Color.pink);
     priormonth=month;
     prioryear=year;
     SwitchMonth(month);
     North2.add(ShowDate=new JLabel(SwitchMonth+"  "+","+" "+String.valueOf(year),SwingConstants.CENTER));//标签居中
     ShowDate.setForeground(Color.pink);
     ShowDate.setFont(new Font("TimesRoman",Font.BOLD,14));
    North2.add(NextMonth=new JButton("下一月"));
    NextMonth.addActionListener(this);
    NextMonth.setActionCommand("next");
    NextMonth.setForeground(Color.pink);
    //左大容器中心位置
    rightCenter.setLayout(new GridLayout(7,7));
    //左大容器中心位置，设置星期标题
    for(int i=0;i<7;i++)
    {
        TopBarTitle[i]=new JLabel();
        TopBarTitle[i].setText(week[i]);
        TopBarTitle[i].setForeground(Color.black);//设置前景色
        TopBarTitle[i].setHorizontalAlignment(0);//设置水平对齐方式
        TopBarTitle[i].setBackground(Color.lightGray);//设置背景色
        //ToToday.setBackground(Color.PINK);
        TopBarTitle[i].setBorder(BorderFactory.createRaisedBevelBorder());//设置边框创建一个具有凸出斜面边缘的边框createRaisedBevelBorder
        rightCenter.add(TopBarTitle[i]);//添加进容器
    }
    //打印到屏幕并且添加监听器
    for(int i=0;i<42;i++)
    {
       ShowDays[i]=new JTextField();
       ShowDays[i].addMouseListener(this);
       ShowDays[i].setEditable(false);//不可编辑表格
       rightCenter.add(ShowDays[i]);
    }
    //打印日历
    PrintMonth(year,month,day);
    //最右框架
    LeftPane=new JPanel(new BorderLayout());
    JPanel leftCenter=new JPanel();
    JPanel leftNorth=new JPanel();
    JPanel leftSouth=new JPanel(new FlowLayout());
    LeftPane.add(leftNorth,BorderLayout.NORTH);
    LeftPane.add(leftCenter,BorderLayout.CENTER);
    LeftPane.add(leftSouth,BorderLayout.SOUTH);
    leftNorth.add(NorthMonthDayYear=new JLabel(">>"+year+"/"+SwitchMonth+"/"+day+"<<"));
    
    key=year+"_"+SwitchMonth+"_"+day;
    NorthMonthDayYear.setForeground(Color.pink);
    NorthMonthDayYear.setFont(new Font("TimesRoman",Font.BOLD,17));
    leftCenter.add(show=new JLabel("写入今天的日志"));
    leftCenter.add(CenterText=new JTextArea());
    CenterText.setLineWrap(true);
    CenterText.setSelectedTextColor(Color.red);
    
    leftSouth.add(SouthSave=new JButton("保存"));
   SouthSave.setBackground(Color.pink);
    SouthSave.addActionListener(this);
    SouthSave.setActionCommand("Save");
    leftSouth.add(SouthDelete=new JButton("删除"));
    SouthDelete.setBackground(Color.pink);
    SouthDelete.addActionListener(this);
    SouthDelete.setActionCommand("Delete");
    this.year=year;
    this.month=month;
    this.day=day;
    //添加内容面板
    Container con=getContentPane();
   JSplitPane split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,RightPane,LeftPane);//水平分割
   con.add(split,BorderLayout.CENTER);
   con.validate();
   //add CenterPane to totepad
   //CenterPane initialize
   setFont(new Font("Times New Roman",Font.PLAIN,12));
   JScrollPane scrollpane=new JScrollPane(CenterText);
   scrollpane.setPreferredSize(new Dimension(220,250));
   leftCenter.add(scrollpane);
   //init randomaccessdile

    }
  //
  public void SwitchMonth(int month)
   {
   	   switch(month)
   	         {
   	         	case 1:
   	         	SwitchMonth="1";break;
   	         	case 2:
   	         	SwitchMonth="2";break;
   	         	case 3:
   	         	SwitchMonth="3";break;
   	         	case 4:
   	         	SwitchMonth="4";break;
   	         	case 5:
   	         	SwitchMonth="5";break;
   	         	case 6:
   	         	SwitchMonth="6";break;
   	         	case 7:
   	         	SwitchMonth="7";break;
   	         	case 8:
   	          SwitchMonth="8";break;
   	          case 9:
   	          SwitchMonth="9";break;
   	          case 10:
   	          SwitchMonth="10";break;
   	          case 11:
   	          SwitchMonth="11";break;
   	          case 12:
   	          SwitchMonth="12";break;
   	         	}
   	}
   	//打印月份
   public void PrintMonth(int year,int month,int day)
   	{
   
   	    int startday=GetStartDay(year,month);
   	
   	int dayinmonth=GetNumOfDaysInMonth(year,month);
   
   	PrintMonthBody(startday,dayinmonth);    	    
   		}  
   	
   	public void PrintMonth(int year,int month)
   	  {
   	  	
   	  	 int dayinmonth=GetNumOfDaysInMonth(year,month);
   	  	 
   	  	 PrintMonthBody(startday,dayinmonth);
   	  	 
   	  }
   	 
   	 public void PrintMonthBody(int startday,int dayinmonth)
   	   {//打印日历上具体的日子
   	   	for(int i=startday,n=1;i<startday+dayinmonth;i++)
   	   	  {
   	   	  	ShowDays[i].setText(""+n);
   	   	  	ShowDays[i].setHorizontalAlignment(0);
   	   	  	if(n==day)
   	   	  	  {
   	   	  	  	ShowDays[i].setForeground(Color.yellow);
   	   	  	  	ShowDays[i].setFont(new Font("TimesRoman",Font.BOLD,20));
   	   	  	  	ShowDays[i].setBackground(Color.pink);
   	   	  	  }
   	   	  	else
   	   	  	 {
   	   	  	 	 ShowDays[i].setFont(new Font("TimesRoman",Font.BOLD,12));
   	   	  	 	 ShowDays[i].setForeground(Color.black);
   	   	  	 	 ShowDays[i].setBackground(Color.pink);
   	   	  	 }  
   	   	  	n++; 
   	   	  }
   	   	//日历空白部分
   	   	for(int i=0;i<startday;i++)
   	   	  {
   	   	    ShowDays[i].setText("");
   	   	    ShowDays[i].setBackground(Color.pink);
   	   	  }  
   	   	for(int i=startday+dayinmonth;i<42;i++)
   	   	  {
   	   	  	 ShowDays[i].setText("");
   	   	  	 ShowDays[i].setBackground(Color.pink);
   	   	  }  
   	   } 
   	   
   public boolean IsLeapYear(int year)
      {
      if((year%400==0)||(year%4==0&&year%100!=0))
          return true;
       else
         return false;   
      }
      
   //月开始的第一天
   public int GetStartDay(int year,int month)
      {
      	 //get total number of day since1/1/0000
      	 int startday0001=-32768;
      	 long totalnumofdays=GetTotalNumOfDays(year,month);
      	 //return the start day
      	 return(int)((totalnumofdays+startday0001)%7);
      }   
   //累加天数
   public long GetTotalNumOfDays(int year,int month) 
     {
     	  long total=0;
     	  //get the total from -32767 to year
     	  for(int i=-32767;i<year;i++)
     	    {
     	    	 if(IsLeapYear(i))
     	    	   total=total+366;
     	    	  else
     	    	    total=total+365; 
     	    }
     	   //添加一月到日历的前一月相加
     	   for(int i=1;i<month;i++)
     	     {
     	     	  total=total+GetNumOfDaysInMonth(year,i);
     	     }
     	   return total;   
     }  
   //日历该月份的天数
   public int GetNumOfDaysInMonth(int year,int month) 
     {
       if(month==1 || month==3 ||month==5 ||month==7 ||month==8 || month==10 ||month==12)
           return 31;
       if(month==4 || month==6||month==9|| month==11)
          return 30;
       if(month==2)
        {
        	if(IsLeapYear(year))
        	  return 29;
        	else
        	  return 28;  
        }   
       return 0; 
     } 
  public void WriteRecord()
    {
    	String content;
    	content=CenterText.getText();
    	int n=content.length();
    	
    	char contentarr[]=new char[n];
    	try
    	 {
    	 	  int i=0;
    	 	  for(i=0;i<n;i++)
    	 	    {
    	 	    	contentarr[i]=content.charAt(i);
    	 	    }
    	 	   File Diary=new File("Diary");
    	 	   Diary.mkdir();//创建目录
    	 	   File myfile=new File("Diary\\"+key+".txt");
    	 	   FileWriter Record=new FileWriter(myfile);
    	 	   if(n==0)
    	 	   {	
    	 	     JOptionPane.showMessageDialog(this,"内容为空无法保存"); 
    	 	   }
    	 	   else
    	 	   {
    	 	   for(i=0;i<contentarr.length;i++)
    	 	     {
    	 	     	  Record.write(contentarr[i]);
    	 	     } 
    	 	   Record.close();
    	 	   JOptionPane.showMessageDialog(this,"保存成功!"); 
    	 	  }  
    	 }
    	catch(IOException ex)
    	 {}
    	  
    }  
  public void ReadRecord()
    {
    	try
    	{
    		String content="";
    		File myfile=new File("Diary\\"+key+".txt");
    		FileReader Record=new FileReader(myfile);
    		if(myfile.exists())
    		 {
    		 	  long b=myfile.length();
    		 	  //char[]= contentarr=new char[b]
    		 	  int n=JOptionPane.showConfirmDialog(this,"今天有日志，要查看吗?",
    		 	     "提示",JOptionPane.YES_NO_CANCEL_OPTION);
    		 	     
    		 	   if(n==JOptionPane.YES_OPTION)
    		 	    {
    		 	    	while((b=Record.read())!=-1)
    		 	    	  {
    		 	    	  	content=content+(char)b;
    		 	    	  }
    		 	    	CenterText.setText(content);  
    		 	    }     
    		 }
    	Record.close();	 
    	}
    catch(IOException ex)
      {
      	CenterText.setText("");
      	show.setText("无日志可读");
      	
      }	
    }
  public void DeleteFile()
    {
    	 String filepath="Diary\\"+key+".txt";
    	 File myfile=new File(filepath);
    	 int n=JOptionPane.showConfirmDialog(this,"确实要删除该内容吗?",
    	     "警告",JOptionPane.YES_NO_CANCEL_OPTION);
    	 if(n==JOptionPane.YES_OPTION) 
    	   {
    	   	 if(myfile.exists())
    	   	   {
    	   	   	  try
    	   	   	   {
    	   	   	   	 myfile.delete();
    	   	   	   }
    	   	   	  catch(Exception e)
    	   	   	   {
    	   	   	   	 e.printStackTrace();
    	   	   	   }
    	   	   	  JOptionPane.showMessageDialog(this,"已经删除!");
    	   	   	  show.setText("无内容");
    	   	   	  //CenterText.setText("Today has not logs."); 
    	   	   }
    	   	 else
    	   	   {
    	   	   	 JOptionPane.showMessageDialog(this,"The file doesn't exist,delete failured!");
    	   	   }  
    	   }   
    }     
  public void AboutActionListenerWay()
    {
    	 try
    	  {
    	  	prioryear=Integer.parseInt(YearText.getText());
    	  	priormonth=MonthCombobox.getSelectedIndex()+1;
    	  	String StrYearText=YearText.getText();
    	  	changeyearmessage=Integer.parseInt(StrYearText);
    	  	changemonthmessage=MonthCombobox.getSelectedIndex()+1;
    	  	monthafterquery=changemonthmessage;
    	  	yearafterquery=changeyearmessage;
    	  	SwitchMonth(changemonthmessage);
    	  	ShowDate.setText(SwitchMonth+" "+","+" "+String.valueOf(changeyearmessage));
    	  	PrintMonth(changeyearmessage,changemonthmessage);
    	  	ischange=true;
    	  }
    	 catch(Exception ee)
    	  {
    	  	 JOptionPane.showMessageDialog(this,"This input format doesn't match",
    	  	       "Eoorr",JOptionPane.ERROR_MESSAGE);
    	  } 
    }  
    
  //do actionlisteneer things
  public void actionPerformed(ActionEvent eAction)
   {
   	  String ActionCommand=eAction.getActionCommand();
   	  //Handle button events
   	  if(eAction.getSource()instanceof JButton)
   	   {
   	   	 //Handle button events
   	   	 if("查询".equals(ActionCommand))
   	   	   {
   	   	   	try
   	   	   	 {
   	   	   	   AboutActionListenerWay();
   	   	   	 }
   	   	   	catch(Exception ee)
   	   	   	  {
   	   	   	  	 JOptionPane.showMessageDialog(this,"The input format doesn't match",
   	   	   	  	    "Error",JOptionPane.ERROR_MESSAGE);
   	   	   	  } 
   	   	   }
   	   	  if("prior".equals(ActionCommand))
   	   	    {
   	   	    	 if(priormonth>1)
   	   	    	   {
   	   	    	   	 priormonth=priormonth-1;
   	   	    	   }
   	   	    	 else
   	   	    	   {
   	   	    	   	  priormonth=12;
   	   	    	   	  prioryear=prioryear-1;
   	   	    	   }  
   	   	    	 PrintMonth(prioryear,priormonth,day);
   	   	    	 SwitchMonth(priormonth);
   	   	    	 ShowDate.setText(SwitchMonth+","+prioryear);
   	   	    	 NorthMonthDayYear.setText(">>"+SwitchMonth+"/"+day+"/"+prioryear+"<<");
   	   	    	 key=prioryear+"_"+SwitchMonth+"_"+day;
   	   	    	 ischange_priornext=true;
   	    
   	   	    } 
   	   	   //Handle next month
   	   	   if("next".equals(ActionCommand))
   	   	     {
   	   	     	 if(priormonth<12)
   	   	     	   {
   	   	     	   	 priormonth=priormonth+1;
   	   	     	   }
   	   	     	 else
   	   	     	   {
   	   	     	   	  priormonth=1;
   	   	     	   	  prioryear=prioryear+1;
   	   	     	   }  
   	   	     	 PrintMonth(prioryear,priormonth,day);
   	   	     	 SwitchMonth(priormonth);
   	   	     	 ShowDate.setText(SwitchMonth+","+prioryear);
   	   	     	 NorthMonthDayYear.setText(">>"+SwitchMonth+"/"+day+"/"+prioryear+"<<");
   	   	     	 key=prioryear+"_"+SwitchMonth+"_"+day;
   	   	     	 ischange_priornext=true; 
   	   	     } 
   	   	    //Handle to "GO to today"
   	   	    if("Go to today".equals(ActionCommand))
   	   	      {
   	   	      	 PrintMonth(year,month,day);
   	   	      	 YearText.setText(" ");
   	   	      	 MonthCombobox.setSelectedIndex(0);
   	   	      	 SwitchMonth(month);
   	   	      	 ShowDate.setText(SwitchMonth+" "+","+" "+String.valueOf(year));
   	   	      	 NorthMonthDayYear.setText(">>"+year+"/"+SwitchMonth+"/"+day+" "+"<<");
   	   	      	 key=year+"_"+SwitchMonth+"_"+day;
   	   	      	 priormonth=month;
   	   	      	 prioryear=year;
   	   	      	 ischange=false;
   	   	      }
   	   	     //Handle the "Save"
   	   	     if("Save".equals(ActionCommand))
   	   	       {
   	   	       	  WriteRecord();
   	   	       } 
   	   	    if("Delete".equals(ActionCommand))
   	   	      {
   	   	      	 DeleteFile();
   	   	      	 CenterText.setText("");
   	   	      }  
   	   }
   	 //Handle JTextField events
   	 if(eAction.getSource()instanceof JTextField)
   	   {
   	   	//Handle the query
   	   	  AboutActionListenerWay();
   	   }
   }  
   
  public void mousePressed(MouseEvent eMouse)
   {
   	 int day;
   	 try
   	  {//Handle ShowDays[]events
   	    if(ischange==false)
   	      {
   	      	 JTextField source=(JTextField)eMouse.getSource();
   	      	 day=Integer.parseInt(source.getText());
   	      	 if(ischange_priornext==false)
   	      	   {
   	      	   	  NorthMonthDayYear.setText(">>"+SwitchMonth+"/"+day+"/"+year+"<<");
   	      	   	  key=year+"_"+SwitchMonth+"_"+day;
   	      	   }
   	      	 else
   	      	   {
   	      	   	 NorthMonthDayYear.setText(">>"+SwitchMonth+"/"+day+"/"+prioryear+"<<");
   	      	   	 key=prioryear+"_"+SwitchMonth+"_"+day;
   	      	   }  
   	      }
   	    else
   	    {
   	      JTextField source=(JTextField)eMouse.getSource();
   	     day=Integer.parseInt(source.getText());
   	    if(ischange_priornext==false)
   	      {
   	      	SwitchMonth(changemonthmessage);
   	      	NorthMonthDayYear.setText(">>"+SwitchMonth+"<"+day+","+changeyearmessage+"<<");
   	      	key=changeyearmessage+"_"+SwitchMonth+"_"+day;
   	      }    
   	    else
   	     {
   	     	SwitchMonth(priormonth);
   	     	NorthMonthDayYear.setText(">>"+SwitchMonth+"/"+day+"/"+prioryear+"<<");
   	     	key=prioryear+"_"+SwitchMonth+"_"+day;
   	     }  
   	  }
   	  ReadRecord(); 
    }
  catch(Exception ee)
   {}
    
  }
   public void mouseClicked(MouseEvent e)
    {}
   public void mouseReleased(MouseEvent e)
    {}
   public void mouseEntered(MouseEvent e)
    {}
   public void mouseExited(MouseEvent e)
    {}
   public void SaveLog(int year,int month,int day)
     {}
   public static void main(String args[])throws Exception
    {
    	 Calendar calendar=Calendar.getInstance();// 使用默认时区和语言环境获得一个日历getInstance
    	 int y=calendar.get(Calendar.YEAR);
    	 int m=calendar.get(Calendar.MONTH)+1;//月份从0开始，所以要加一
    	 int d=calendar.get(Calendar.DAY_OF_MONTH);
    	 calendar frame=new calendar(y,m,d);
    	 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	 frame.setSize(560,400);
    	 frame.setLocation(100,100);
    	 frame.setVisible(true);
    	 frame.setResizable(false);
    	 frame.ReadRecord(); 
    }
}
