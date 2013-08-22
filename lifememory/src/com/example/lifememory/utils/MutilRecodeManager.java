package com.example.lifememory.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.media.AudioManager;

public class MutilRecodeManager {
	public static MutilRecodeManager instance = null;
	public MutilRecodeManager() {};
	public static MutilRecodeManager getInstance() {
		if(instance == null) {
			instance = new MutilRecodeManager();
		}
		return instance;
	}
	
	
	/** �ϲ����е���Ƶ�ļ� */
    public void mergeAllAmrFiles(List<File> recodeFiles, String filePath){ 
            // ������Ƶ�ļ�,�ϲ����ļ�������
              File mergeFile = new File(filePath);
              FileOutputStream fileOutputStream = null;
              
              if(!mergeFile.exists()){  
                  try {  
                      mergeFile.createNewFile();  
                  } catch (IOException e){  
                      e.printStackTrace();  
                  }  
              }
      
              try {  
                  fileOutputStream = new FileOutputStream(mergeFile);  
              } catch (IOException e) {  
                  e.printStackTrace();  
              } 
              
              //list����Ϊ��ͣ¼�� �������ļ���¼���ļ������֣��м伸���ļ��ļ�ȥǰ���6���ֽ�ͷ�ļ�  
              for(int index = 0; index < recodeFiles.size(); index++){  
                  File file = recodeFiles.get(index);
                  try {  
                      FileInputStream fileInputStream=new FileInputStream(file);  
                      byte  []myByte = new byte[fileInputStream.available()];
                      //�ļ�����  
                      int length = myByte.length;
        
                      //ͷ�ļ�  
                      if(index == 0){  
                          while(fileInputStream.read(myByte) != -1){ 
                              fileOutputStream.write(myByte);
                              //fileOutputStream.write(myByte, 0,length);
                          }
                      }
                      
                      //֮����ļ���ȥ��ǰ��6���ֽڣ�ͷ�ļ��� 
                      else{  
                          while(fileInputStream.read(myByte) != -1){            
                              fileOutputStream.write(myByte, 6, length - 6);  
                          }
                      }
                
                      fileOutputStream.flush();
                      fileInputStream.close();
                      //�ϲ�֮��ɾ���ļ�
                      file.delete();
                  } catch (Exception e) {  
                      e.printStackTrace();  
                  }  
              }  
              
              //������ر��� 
              try {
                  fileOutputStream.flush();
                  fileOutputStream.close();  
              } catch (IOException e) {  
                  // TODO Auto-generated catch block  
                  e.printStackTrace();  
              }
        }
    
    
    
    public void getInputCollection(List<File> recodeFiles, String filePath){  
        
        
          
        // ������Ƶ�ļ�,�ϲ����ļ�������   
        File file1=new File(filePath);  
        FileOutputStream fileOutputStream = null;  
           
        if(!file1.exists()){  
            try {  
                file1.createNewFile();  
            } catch (IOException e) {  
                // TODO Auto-generated catch block   
                e.printStackTrace();  
            }  
        }  
        try {  
            fileOutputStream=new FileOutputStream(file1);  
  
        } catch (IOException e) {  
            // TODO Auto-generated catch block   
            e.printStackTrace();  
        }  
        //list����Ϊ��ͣ¼�� �������� ����¼���ļ������֣��м伸���ļ��ļ�ȥǰ���6���ֽ�ͷ�ļ�   
          
          
          
      
        for(int i=0;i<recodeFiles.size();i++){  
            File file = recodeFiles.get(i);  
            try {  
                FileInputStream fileInputStream=new FileInputStream(file);  
                byte  []myByte=new byte[fileInputStream.available()];  
                //�ļ�����   
                int length = myByte.length;  
          
                //ͷ�ļ�   
                if(i==0){  
                        while(fileInputStream.read(myByte)!=-1){  
                                fileOutputStream.write(myByte, 0,length);  
                            }  
                        }  
                      
                //֮����ļ���ȥ��ͷ�ļ��Ϳ�����   
                else{  
                    while(fileInputStream.read(myByte)!=-1){  
                          
                        fileOutputStream.write(myByte, 6, length-6);  
                    }  
                }  
                  
                fileOutputStream.flush();  
                fileInputStream.close();  
                System.out.println("�ϳ��ļ����ȣ�"+file1.length());  
              
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
              
              
              
            }  
        //������ر���   
        try {  
            fileOutputStream.close();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block   
            e.printStackTrace();  
        }  
          
      
    }  

}
