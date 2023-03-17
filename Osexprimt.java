package osexprimt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class Jobinput{
	public int JobsID;//作业序号
	public int Intimes;//作业请求时间
	public int  InstrucNum;//作业包含的程序指令数目
	public Jobinput(){
		JobsID=0;
		Intimes=0;
		InstrucNum=0;
	}
	public Jobinput(int JobsID,int Intimes,int InstrucNum) {
		this.JobsID=JobsID;
		this.Intimes=Intimes;
		this.InstrucNum=InstrucNum;
	}

}
class Jobinstruc{
	public int Instruc_ID;//指令编号
	public int Instruc_State;//用户程序指令的类型
	public int Size;
	public Jobinstruc() {
		Instruc_ID=0;
		Instruc_State=0;

	}
	public Jobinstruc(int Instruc_ID,int Instruc_State) {
		this.Instruc_ID=Instruc_ID;
		this.Instruc_State=Instruc_State;
	}
}
class WriteFile{

	public static void writeintofile(String str) {
		str=str+"\r\n";
		 String choice;
		FileOutputStream outstream=null;
		byte[] buffer=new byte[] {};
		if(Osexprimt.filenumber==1) {choice="output1\\outfst.txt";}
		else {choice="output2\\outfst.txt";}
		File outfile=new File(choice);
		try {
			if(!outfile.exists()) {outfile.createNewFile();}
			outstream=new FileOutputStream(outfile,true);
			buffer=str.getBytes();
			outstream.write(buffer);
			outstream.flush();
			outstream.close();
		} catch (IOException e) {
				// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public static void movename() {
		String choice;
		if(Osexprimt.filenumber==1) {choice="output1\\outfst.txt";}
		else {choice="output2\\outfst.txt";}
		File nfile=new File(choice);
		try {
			if(Osexprimt.filenumber==1) {Files.move(nfile.toPath(), new File("output1\\ProcessResults-"+(cpu.Time-1)+"-LZ.txt").toPath(), StandardCopyOption.REPLACE_EXISTING);}
			else {Files.move(nfile.toPath(), new File("output2\\ProcessResults-"+(cpu.Time-1)+"-LZ.txt").toPath(), StandardCopyOption.REPLACE_EXISTING);}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}

@SuppressWarnings("serial")
public class Osexprimt extends JFrame{
	 private JButton inputchoice1=new JButton("input1");
	    private JButton inputchoice2=new JButton("input2");
	    private JButton timelyrequest=new JButton("实时作业请求");
	    public Osexprimt(){
	        JPanel panel=new JPanel();
	        panel.add(inputchoice1);
	        panel.add(inputchoice2);
	        panel.add(timelyrequest);
	        add(panel,BorderLayout.SOUTH);
	        inputchoice1.addActionListener(new setinput1());
	        inputchoice2.addActionListener(new setinput2() );
	        timelyrequest.addActionListener(new timelyjobsinput());
	    }
	    class timelyjobsinput implements ActionListener{
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		Random random=new Random();
	    		int innum=random.nextInt(21)+10;//指令条数为10——30之间的随机数
	    		int n=stopmessage.inputjobsnum+1;
	    		cpu.queue.add(String.valueOf(n));
	    		J[n-1].InstrucNum=innum;
	    		J[n-1].Intimes=cpu.Time;
	    		for(int i=0;i<=innum;i++) {
	    			cpu.J1[n][i].Instruc_ID=i+1;
	    			cpu.J1[n][i].Instruc_State=0;
	    		}
	    		stopmessage.inputjobsnum++;
	    		System.out.println("实时作业请求");
	    	}
	    }
	    class setinput1 implements ActionListener{
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	Thread JRT=new Thread(new JobRequestThread());
	    		Thread PST=new Thread(new ProcessingSchedulingThread());	
	        	fileName = "input1\\";
	        	filenumber=1;
	        	read(fileName+"jobs-input.txt",J);
	    		JRT=new JobRequestThread(J);
	    		PST=new ProcessingSchedulingThread(J);
	    		PST.start();
	    		JRT.start();
	        }
	    }
	    class setinput2 implements ActionListener{
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	Thread JRT=new Thread(new JobRequestThread());
	    		Thread PST=new Thread(new ProcessingSchedulingThread());	
	        	fileName = "input2\\";
	        	filenumber=2;
	        	read(fileName+"jobs-input.txt",J);
	    		JRT=new JobRequestThread(J);
	    		PST=new ProcessingSchedulingThread(J);
	    		PST.start();
	    		JRT.start();
	        }
	    }
	public static String fileName;
	public static int filenumber;
	public static Jobinput J[]=new Jobinput[10];//并发作业请求文件
	public static void read(String fileName,Jobinput J[]) {
		int size=0;
		try (Scanner sc = new Scanner(new FileReader(fileName))) {
		   while (sc.hasNextLine()) {
			   stopmessage.inputjobsnum++;
			   String line = sc.nextLine();
			   String ss[]=line.split(",");
		       J[size].JobsID=Integer.parseInt(ss[0]);
		       J[size].Intimes=Integer.parseInt(ss[1]);
		       J[size].InstrucNum=Integer.parseInt(ss[2]);
		       size++;
		   }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			}
	}
	public static void main(String []args) {
		Osexprimt frame=new Osexprimt();
		frame.setTitle("choose one");
	    frame.setSize(300,100);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
		for(int i=1;i<100;i++) {
				cpu.PCB[i]=new pcb();
		}
		for(int size=0;size<10;size++) {
			J[size]=new Jobinput();
		}
	}
}
class stopmessage{
	public static int inputjobsnum;
	public static int finishjobsnum;
	public static String str[]=new String[100];
	public stopmessage() {
		inputjobsnum=0;
		finishjobsnum=0;
	}
}
class cpu extends Thread{
	public static int Time;//CPU时钟
	public static Queue<String> queue=new LinkedList<String>();//后备队列
	public static Queue<String> queue_ready=new LinkedList<String>();//就绪队列
	public static Jobinstruc J1[][]=new Jobinstruc[10][30];//作业程序指令文件
	public static pcb PCB[]=new pcb[100];//进程控制块,PCB空间足够大，但在运行时实际仅有30个PCB空间
	public cpu() {
		Time=0;
	}
}
class pcb{
	public  int ProID;//进程编号
	public int Priority;//进程优先数
	public int InTimes;//进程创建时间
	public int EndTimes;//进程结束时间
	public int PSW;//进程状态 数字0代表就绪态,数字1代表运行态
	public int RunTimes;//进程运行时间列表
	public int TurnTimes;//进程周转时间统计
	public int InstrucNum;//进程包含的指令数目,可以用来表示进程剩余未运行的指令条数
	public int PC;//程序计数器信息
	public int IR;//程序寄存器信息
	public int timeleft;//时间片余量
	public int placeholder;//该位PCB是否占用
	public int requesttime;//作业请求时间
	public int RqTimes;//进入就绪队列时间
	public int RqNum;//在就绪队列编号
	public pcb() {
		placeholder=0;
	}
}
class ProcessingSchedulingThread extends Thread{//仿真系统时钟中断
	public Jobinput J[]=new Jobinput[10];
	public ProcessingSchedulingThread() {
	}
	public ProcessingSchedulingThread(Jobinput J[]) {
		for(int i=0;i<10;i++) {
			this.J[i]=new Jobinput();
			this.J[i]=J[i];
		}
	}
	public void run() {
		int num=0;

		System.out.println("进程调度事件:");
		String str="进程调度事件:";
		WriteFile.writeintofile(str);
		while(true) {
			cpu.Time=cpu.Time+1;
			try {
			sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			while(cpu.queue.peek()!=null) {//进程创建
				if(num<30) {//最多同时30个pcb空间被占用
					System.out.println(cpu.Time+":[创建进程:"+cpu.queue.peek()+"]");
					str=cpu.Time+":[创建进程:"+cpu.queue.peek()+"]";
					WriteFile.writeintofile(str);
					cpu.PCB[Integer.valueOf(cpu.queue.peek())].ProID=Integer.valueOf(cpu.queue.peek());
					cpu.PCB[Integer.valueOf(cpu.queue.peek())].IR=0;
					cpu.PCB[Integer.valueOf(cpu.queue.peek())].PC=1;
					cpu.PCB[Integer.valueOf(cpu.queue.peek())].InstrucNum=J[Integer.valueOf(cpu.queue.peek())-1].InstrucNum;
					cpu.PCB[Integer.valueOf(cpu.queue.peek())].timeleft=3;
					cpu.PCB[Integer.valueOf(cpu.queue.peek())].RunTimes=J[Integer.valueOf(cpu.queue.peek())-1].InstrucNum;
					cpu.PCB[Integer.valueOf(cpu.queue.peek())].InTimes=cpu.Time;
					cpu.PCB[Integer.valueOf(cpu.queue.peek())].placeholder=1;
					cpu.PCB[Integer.valueOf(cpu.queue.peek())].requesttime=J[Integer.valueOf(cpu.queue.peek())-1].Intimes;
					System.out.println(cpu.Time+":[进入就绪队列:"+cpu.queue.peek()+","+J[Integer.valueOf(cpu.queue.peek())-1].InstrucNum+"]");
					str=cpu.Time+":[进入就绪队列:"+cpu.queue.peek()+","+J[Integer.valueOf(cpu.queue.peek())-1].InstrucNum+"]";
					WriteFile.writeintofile(str);
					cpu.PCB[Integer.valueOf(cpu.queue.peek())].RqTimes=cpu.Time;
					
					cpu.queue_ready.add(cpu.queue.peek());
					cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].PSW=0;//进程进入就绪态
					cpu.queue.poll();
					num++;
				}
				else {
					System.out.println(cpu.Time+":[PCB空间不够]");
					str=cpu.Time+":[PCB空间不够]";
					WriteFile.writeintofile(str);
					break;
				}
			}
			if(stopmessage.finishjobsnum==stopmessage.inputjobsnum) {
				System.out.println("状态统计信息:");
				str="状态统计信息:";
				WriteFile.writeintofile(str);
				for(int i=0;i<stopmessage.inputjobsnum;i++) {
					System.out.println(stopmessage.str[i]);
					WriteFile.writeintofile(stopmessage.str[i]);
				}
				WriteFile.movename();
				break;
			}
			if(cpu.queue_ready.peek()==null) {
				System.out.println(cpu.Time+":[cpu空闲]");
				str=cpu.Time+":[cpu空闲]";
				WriteFile.writeintofile(str);
			}
			if(cpu.queue_ready.peek()!=null) {
				cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].PSW=1;//进程进入运行态
				System.out.println(cpu.Time+":[运行进程:"+cpu.queue_ready.peek()+","+cpu.J1[Integer.valueOf(cpu.queue_ready.peek())][cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].IR].Instruc_ID+","+"计算"/*cpu.J1[Integer.valueOf(cpu.queue_ready.peek())][cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].IR].Instruc_State*/+"]");
				str=cpu.Time+":[运行进程:"+cpu.queue_ready.peek()+","+cpu.J1[Integer.valueOf(cpu.queue_ready.peek())][cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].IR].Instruc_ID+","+"计算"+"]";
				WriteFile.writeintofile(str);
				cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].InstrucNum--;
				cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].IR++;
				if(cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].InstrucNum==0) {//进程撤销
					System.out.println(cpu.Time+":[终止进程:"+cpu.queue_ready.peek()+"]");
					str=cpu.Time+":[终止进程:"+cpu.queue_ready.peek()+"]";
					WriteFile.writeintofile(str);
					cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].EndTimes=cpu.Time;
					cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].TurnTimes=cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].EndTimes-cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].InTimes;
					cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].placeholder=0;
					stopmessage.str[stopmessage.finishjobsnum]=cpu.Time+":["+cpu.queue_ready.peek()+":"+cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].requesttime+"+"+cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].InTimes+"+"+cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].TurnTimes+"]";
					num--;//撤销进程占用PCB空间
					stopmessage.finishjobsnum++;
					cpu.queue_ready.poll();
					continue;
				}
					if(cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].InstrucNum!=0&&cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].IR%3==0) {
						cpu.queue_ready.add(cpu.queue_ready.peek());
						cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].PSW=0;//进程重新进入就绪态
						System.out.println(cpu.Time+":[重新进入就绪队列:"+cpu.queue_ready.peek()+","+cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].InstrucNum+"]");
						str=cpu.Time+":[重新进入就绪队列:"+cpu.queue_ready.peek()+","+cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].InstrucNum+"]";
						WriteFile.writeintofile(str);
						cpu.queue_ready.poll();
					}
				}
			}

		}
		
	}
	


class JobRequestThread extends Thread{//仿真鼠标双击事件中断
	//public  Jobinstruc J1[][]=new Jobinstruc[10][20];//作业程序指令文件
	public Jobinput J[]=new Jobinput[10];
	public JobRequestThread() {
	}
	public JobRequestThread(Jobinput J[]) {
		for(int i=0;i<10;i++) {
			this.J[i]=new Jobinput();
			this.J[i]=J[i];
		}
	}
	public void read(String fileName,Jobinstruc J1[][],int JobsID) {
		int size=0;
		try (Scanner sc = new Scanner(new FileReader(fileName))) {
		   while (sc.hasNextLine()) {
			   String line = sc.nextLine();
			   String ss[]=line.split(",");
		       J1[JobsID][size].Instruc_ID=Integer.parseInt(ss[0]);
		       J1[JobsID][size].Instruc_State=Integer.parseInt(ss[1]);
		       size++;
		   }

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			}

	}
	public void run() {
		//<String> queue=new LinkedList<String>();
		int j=0;
		for(int m=0;m<10;m++) {
			for(int n=0;n<30;n++) {
				cpu.J1[m][n]=new Jobinstruc();
			}
		}
		while(true) {
			//cpu.Time=cpu.Time+1;
			
			try {
				JobRequestThread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(cpu.Time%5==0) {
			for(int i=j;J[i].JobsID!=0;i++) {
				if(J[i].Intimes<=cpu.Time) {
					System.out.println(cpu.Time+":[新增作业:"+J[i].JobsID+","+J[i].Intimes+","+J[i].InstrucNum+"]");
					String str=cpu.Time+":[新增作业:"+J[i].JobsID+","+J[i].Intimes+","+J[i].InstrucNum+"]";
					WriteFile.writeintofile(str);
					String s=String.valueOf(J[i].JobsID);
					cpu.queue.add(s);
					read(Osexprimt.fileName+String.valueOf(J[i].JobsID)+".txt",cpu.J1,J[i].JobsID);
					j++;
					
				}
			}
			}
			if(stopmessage.finishjobsnum==stopmessage.inputjobsnum) {
				
				break;
			}
		}	
	}
}
