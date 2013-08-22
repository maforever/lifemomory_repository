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
	
	
	/** 合并所有的音频文件 */
    public void mergeAllAmrFiles(List<File> recodeFiles, String filePath){ 
            // 创建音频文件,合并的文件放这里
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
              
              //list里面为暂停录音 所产生的几段录音文件的名字，中间几段文件的减去前面的6个字节头文件  
              for(int index = 0; index < recodeFiles.size(); index++){  
                  File file = recodeFiles.get(index);
                  try {  
                      FileInputStream fileInputStream=new FileInputStream(file);  
                      byte  []myByte = new byte[fileInputStream.available()];
                      //文件长度  
                      int length = myByte.length;
        
                      //头文件  
                      if(index == 0){  
                          while(fileInputStream.read(myByte) != -1){ 
                              fileOutputStream.write(myByte);
                              //fileOutputStream.write(myByte, 0,length);
                          }
                      }
                      
                      //之后的文件，去掉前面6个字节（头文件） 
                      else{  
                          while(fileInputStream.read(myByte) != -1){            
                              fileOutputStream.write(myByte, 6, length - 6);  
                          }
                      }
                
                      fileOutputStream.flush();
                      fileInputStream.close();
                      //合并之后删除文件
                      file.delete();
                  } catch (Exception e) {  
                      e.printStackTrace();  
                  }  
              }  
              
              //结束后关闭流 
              try {
                  fileOutputStream.flush();
                  fileOutputStream.close();  
              } catch (IOException e) {  
                  // TODO Auto-generated catch block  
                  e.printStackTrace();  
              }
        }
    
    
    
    public void getInputCollection(List<File> recodeFiles, String filePath){  
        
        
          
        // 创建音频文件,合并的文件放这里   
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
        //list里面为暂停录音 所产生的 几段录音文件的名字，中间几段文件的减去前面的6个字节头文件   
          
          
          
      
        for(int i=0;i<recodeFiles.size();i++){  
            File file = recodeFiles.get(i);  
            try {  
                FileInputStream fileInputStream=new FileInputStream(file);  
                byte  []myByte=new byte[fileInputStream.available()];  
                //文件长度   
                int length = myByte.length;  
          
                //头文件   
                if(i==0){  
                        while(fileInputStream.read(myByte)!=-1){  
                                fileOutputStream.write(myByte, 0,length);  
                            }  
                        }  
                      
                //之后的文件，去掉头文件就可以了   
                else{  
                    while(fileInputStream.read(myByte)!=-1){  
                          
                        fileOutputStream.write(myByte, 6, length-6);  
                    }  
                }  
                  
                fileOutputStream.flush();  
                fileInputStream.close();  
                System.out.println("合成文件长度："+file1.length());  
              
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
              
              
              
            }  
        //结束后关闭流   
        try {  
            fileOutputStream.close();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block   
            e.printStackTrace();  
        }  
          
      
    }  

}
