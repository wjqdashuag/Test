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
	public int JobsID;//��ҵ���
	public int Intimes;//��ҵ����ʱ��
	public int  InstrucNum;//��ҵ�����ĳ���ָ����Ŀ
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
	public int Instruc_ID;//ָ����
	public int Instruc_State;//�û�����ָ�������
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
				// TODO �Զ����ɵ� catch ��
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
}

@SuppressWarnings("serial")
public class Osexprimt extends JFrame{
	 private JButton inputchoice1=new JButton("input1");
	    private JButton inputchoice2=new JButton("input2");
	    private JButton timelyrequest=new JButton("ʵʱ��ҵ����");
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
	    		int innum=random.nextInt(21)+10;//ָ������Ϊ10����30֮��������
	    		int n=stopmessage.inputjobsnum+1;
	    		cpu.queue.add(String.valueOf(n));
	    		J[n-1].InstrucNum=innum;
	    		J[n-1].Intimes=cpu.Time;
	    		for(int i=0;i<=innum;i++) {
	    			cpu.J1[n][i].Instruc_ID=i+1;
	    			cpu.J1[n][i].Instruc_State=0;
	    		}
	    		stopmessage.inputjobsnum++;
	    		System.out.println("ʵʱ��ҵ����");
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
	public static Jobinput J[]=new Jobinput[10];//������ҵ�����ļ�
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
	public static int Time;//CPUʱ��
	public static Queue<String> queue=new LinkedList<String>();//�󱸶���
	public static Queue<String> queue_ready=new LinkedList<String>();//��������
	public static Jobinstruc J1[][]=new Jobinstruc[10][30];//��ҵ����ָ���ļ�
	public static pcb PCB[]=new pcb[100];//���̿��ƿ�,PCB�ռ��㹻�󣬵�������ʱʵ�ʽ���30��PCB�ռ�
	public cpu() {
		Time=0;
	}
}
class pcb{
	public  int ProID;//���̱��
	public int Priority;//����������
	public int InTimes;//���̴���ʱ��
	public int EndTimes;//���̽���ʱ��
	public int PSW;//����״̬ ����0�������̬,����1��������̬
	public int RunTimes;//��������ʱ���б�
	public int TurnTimes;//������תʱ��ͳ��
	public int InstrucNum;//���̰�����ָ����Ŀ,����������ʾ����ʣ��δ���е�ָ������
	public int PC;//�����������Ϣ
	public int IR;//����Ĵ�����Ϣ
	public int timeleft;//ʱ��Ƭ����
	public int placeholder;//��λPCB�Ƿ�ռ��
	public int requesttime;//��ҵ����ʱ��
	public int RqTimes;//�����������ʱ��
	public int RqNum;//�ھ������б��
	public pcb() {
		placeholder=0;
	}
}
class ProcessingSchedulingThread extends Thread{//����ϵͳʱ���ж�
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

		System.out.println("���̵����¼�:");
		String str="���̵����¼�:";
		WriteFile.writeintofile(str);
		while(true) {
			cpu.Time=cpu.Time+1;
			try {
			sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			while(cpu.queue.peek()!=null) {//���̴���
				if(num<30) {//���ͬʱ30��pcb�ռ䱻ռ��
					System.out.println(cpu.Time+":[��������:"+cpu.queue.peek()+"]");
					str=cpu.Time+":[��������:"+cpu.queue.peek()+"]";
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
					System.out.println(cpu.Time+":[�����������:"+cpu.queue.peek()+","+J[Integer.valueOf(cpu.queue.peek())-1].InstrucNum+"]");
					str=cpu.Time+":[�����������:"+cpu.queue.peek()+","+J[Integer.valueOf(cpu.queue.peek())-1].InstrucNum+"]";
					WriteFile.writeintofile(str);
					cpu.PCB[Integer.valueOf(cpu.queue.peek())].RqTimes=cpu.Time;
					
					cpu.queue_ready.add(cpu.queue.peek());
					cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].PSW=0;//���̽������̬
					cpu.queue.poll();
					num++;
				}
				else {
					System.out.println(cpu.Time+":[PCB�ռ䲻��]");
					str=cpu.Time+":[PCB�ռ䲻��]";
					WriteFile.writeintofile(str);
					break;
				}
			}
			if(stopmessage.finishjobsnum==stopmessage.inputjobsnum) {
				System.out.println("״̬ͳ����Ϣ:");
				str="״̬ͳ����Ϣ:";
				WriteFile.writeintofile(str);
				for(int i=0;i<stopmessage.inputjobsnum;i++) {
					System.out.println(stopmessage.str[i]);
					WriteFile.writeintofile(stopmessage.str[i]);
				}
				WriteFile.movename();
				break;
			}
			if(cpu.queue_ready.peek()==null) {
				System.out.println(cpu.Time+":[cpu����]");
				str=cpu.Time+":[cpu����]";
				WriteFile.writeintofile(str);
			}
			if(cpu.queue_ready.peek()!=null) {
				cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].PSW=1;//���̽�������̬
				System.out.println(cpu.Time+":[���н���:"+cpu.queue_ready.peek()+","+cpu.J1[Integer.valueOf(cpu.queue_ready.peek())][cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].IR].Instruc_ID+","+"����"/*cpu.J1[Integer.valueOf(cpu.queue_ready.peek())][cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].IR].Instruc_State*/+"]");
				str=cpu.Time+":[���н���:"+cpu.queue_ready.peek()+","+cpu.J1[Integer.valueOf(cpu.queue_ready.peek())][cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].IR].Instruc_ID+","+"����"+"]";
				WriteFile.writeintofile(str);
				cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].InstrucNum--;
				cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].IR++;
				if(cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].InstrucNum==0) {//���̳���
					System.out.println(cpu.Time+":[��ֹ����:"+cpu.queue_ready.peek()+"]");
					str=cpu.Time+":[��ֹ����:"+cpu.queue_ready.peek()+"]";
					WriteFile.writeintofile(str);
					cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].EndTimes=cpu.Time;
					cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].TurnTimes=cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].EndTimes-cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].InTimes;
					cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].placeholder=0;
					stopmessage.str[stopmessage.finishjobsnum]=cpu.Time+":["+cpu.queue_ready.peek()+":"+cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].requesttime+"+"+cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].InTimes+"+"+cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].TurnTimes+"]";
					num--;//��������ռ��PCB�ռ�
					stopmessage.finishjobsnum++;
					cpu.queue_ready.poll();
					continue;
				}
					if(cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].InstrucNum!=0&&cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].IR%3==0) {
						cpu.queue_ready.add(cpu.queue_ready.peek());
						cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].PSW=0;//�������½������̬
						System.out.println(cpu.Time+":[���½����������:"+cpu.queue_ready.peek()+","+cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].InstrucNum+"]");
						str=cpu.Time+":[���½����������:"+cpu.queue_ready.peek()+","+cpu.PCB[Integer.valueOf(cpu.queue_ready.peek())].InstrucNum+"]";
						WriteFile.writeintofile(str);
						cpu.queue_ready.poll();
					}
				}
			}

		}
		
	}
	


class JobRequestThread extends Thread{//�������˫���¼��ж�
	//public  Jobinstruc J1[][]=new Jobinstruc[10][20];//��ҵ����ָ���ļ�
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
					System.out.println(cpu.Time+":[������ҵ:"+J[i].JobsID+","+J[i].Intimes+","+J[i].InstrucNum+"]");
					String str=cpu.Time+":[������ҵ:"+J[i].JobsID+","+J[i].Intimes+","+J[i].InstrucNum+"]";
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
