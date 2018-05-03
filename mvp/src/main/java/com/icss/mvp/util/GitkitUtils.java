package com.icss.mvp.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.propertyeditors.UUIDEditor;

import com.icss.mvp.entity.GitLogs;

public class GitkitUtils {
	private static String gitFilePath = "D:\\GITFILE\\project";  
	private static Repository repository; 
	private static File root;
	private static Git git;
    
    /**
     * 删除文件
     * @param file
     */
    public static void deleteFolder(String gitFilePath){
    	File file =new File(gitFilePath);
        if(file.isFile() || file.list().length==0){
            boolean flag=file.delete();
            if(!flag) {
            	System.gc();
            	file.delete();
            }
            
        }else{
            File[] files = file.listFiles();
            for(int i=0;i<files.length;i++){
                deleteFolder(files[i].getPath());
                boolean flag = files[i].delete();
                if(!flag) {
                	System.gc();
                	files[i].delete();
                }
            }
        }
    }
    
    /**
     * 从远程仓库克隆代码到本地
     * @param username 账号
     * @param password 密码
     * @param url 远程仓库地址
     * @param no  项目编号
     * @throws Exception
     */
    public static  void setupRepo(String username,String password,String url) throws Exception{
    	root= new File(gitFilePath+"\\"+UUIDUtil.getNew()); 
    	new File(gitFilePath);
    	if(new File(gitFilePath).exists()) {
    		deleteFolder(gitFilePath);
    	}
    		
    	//设置远程服务器上的用户名和密码
        UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider(username,password);
        //克隆代码库命令
        CloneCommand cloneCommand = Git.cloneRepository();
    	git= cloneCommand.setURI(url) //设置远程URI
                 .setBranch("master") //设置clone下来的分支
                 .setDirectory(root) //设置下载存放路径
                 .setCredentialsProvider(usernamePasswordCredentialsProvider) //设置权限验证
                 .call();
//    	repository = git.getRepository();
    	git.close();
    }
      
     
    public static void init(){  
        try {  
            git =Git.open(root);  
            repository = git.getRepository();  
              
        } catch (IOException e) {  
            e.printStackTrace();  
        }   
    }  
    
    /**
     * 日志采集
     * @param str 开始日期
     * @param end 结束日期
     * @param no  项目编号
     * @return
     * @throws Exception
     */
    public static List<GitLogs> gatherCodeByTime(Date str,Date end,String no) throws Exception{  
    	init();
        List<GitLogs> gitLogs =new ArrayList<GitLogs>();
        Iterable<RevCommit> commits = git.log().call();//获取git所有日志信息
        for(RevCommit commit:commits){
//        	 String version=commit.getName();//版本号  
//        	 String author = commit.getAuthorIdent().getName();  
//        	 String email =commit.getAuthorIdent().getEmailAddress();  
        	 Date date = commit.getAuthorIdent().getWhen();//时间  
        	 if(date.before(end)&&date.after(str)) { //提交时间在某个时间段
        		 GitLogs gitLog= getChangeLog(commit);
        		 gitLog.setNo(no);
        		 gitLogs.add(gitLog);
        		 System.out.println(gitLog.getDelNum() + "=======>"+gitLog.getModifyNum());
        	 }
        }
        git.clean();
        git.close();
        deleteFolder(gitFilePath);
		return  gitLogs; 
    }  
    /**
     * 此版本和上个版本比较差异计算修改代码量和删除代码量
     * @param commit 提交的日志信息
     * @return
     * @throws GitAPIException
     * @throws IOException
     */
    public static GitLogs getChangeLog(RevCommit commit) throws IOException {
    	 ByteArrayOutputStream out = new ByteArrayOutputStream();
    	 DiffFormatter df =null;
    	 GitLogs gitLogs=new GitLogs(); 
    	    
    	try {
			
			gitLogs.setId(UUIDUtil.getNew());
			gitLogs.setAuthor(commit.getAuthorIdent().getName());
			gitLogs.setColDate(new Date());
			gitLogs.setRevision(commit.getName());
			gitLogs.setMessage(commit.getFullMessage());
			gitLogs.setCommitime(commit.getAuthorIdent().getWhen());
			RevCommit revCommit = getParentContent(commit.getName());
			if(revCommit!=null) {
//    	    AbstractTreeIterator newTree = prepareTreeParser(commitList.get(0));  
//    	    AbstractTreeIterator oldTree = prepareTreeParser(commitList.get(1));
				AbstractTreeIterator oldTree= prepareTreeParser(revCommit);
				AbstractTreeIterator newTree= prepareTreeParser(commit);
				List<DiffEntry> diff =git.diff().setOldTree(oldTree).setNewTree(newTree).setShowNameAndStatusOnly(true).call();
				gitLogs.setFileNum(diff.size());
				df = new DiffFormatter(out);
			    //设置比较器为忽略空白字符对比（Ignores all whitespace）  
			    df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);  
			    df.setRepository(git.getRepository());   
			    System.out.println("------------------------------start-----------------------------");  
			    int modifyNum = 0;  
			    int delNum = 0;  
			    //每一个diffEntry都是第个文件版本之间的变动差异  
			    for (DiffEntry diffEntry : diff) {   
			        //打印文件差异具体内容  
			        df.format(diffEntry);    
			        String diffText = out.toString("UTF-8");    
			        System.out.println("==================>"+diffText);    
			          
			        //获取文件差异位置，从而统计差异的行数，如增加行数，减少行数  
			        FileHeader fileHeader = df.toFileHeader(diffEntry);  
			        List<HunkHeader> hunks = (List<HunkHeader>) fileHeader.getHunks();  
			        for(HunkHeader hunkHeader:hunks){  
			            EditList editList = hunkHeader.toEditList();  
			            for(Edit edit : editList){  
			                delNum += edit.getEndA()-edit.getBeginA();  //删除行数
			                modifyNum += edit.getEndB()-edit.getBeginB();  //增加行数
			                  
			            }  
			        }
			    
			        System.out.println("modifyNum="+modifyNum);  
			        System.out.println("delNum="+delNum);  
			        System.out.println("------------------------------end-----------------------------");
			    }
			    //gitLogs.setDelNum(delNum);
			    gitLogs.setModifyNum(modifyNum);
			    
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CorruptObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MissingObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			 out.reset();
			 out.close();
		     if(df!=null) {
		    	 df.close();
		     }
		    
		}
		return gitLogs;   
    }
          
    //日志信息转化为  RevTree
    public static AbstractTreeIterator prepareTreeParser(RevCommit commit){  
        System.out.println(commit.getId()); 
        try (RevWalk walk = new RevWalk(repository);) {  
            System.out.println(commit.getTree().getId());  
            RevTree tree = walk.parseTree(commit.getTree().getId());  
  
            CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();  
            try (ObjectReader oldReader = repository.newObjectReader()) {  
                oldTreeParser.reset(oldReader, tree.getId());  
            }  
            walk.dispose();  
            walk.close();
            return oldTreeParser;  
    }catch (Exception e) {  
        // TODO: handle exception  
    }
        return null;  
    }  
    
    /** 
     * 获取上版本的日志信息
     * @param revision    版本号 
     * @param gitFilePath 文件路径 
     * @return 
     */  
    public static RevCommit getParentContent(String revision) {  
    	RevWalk walk = new RevWalk(repository);
        try {  
                
            ObjectId objId = repository.resolve(revision);//通过版本号分解，得到版本对象(String>>>object)  
            RevCommit revCommit = walk.parseCommit(objId); 
            if(revCommit.getParents().length>0 ) {
            	String preVision = revCommit.getParent(0).getName();//反回上一版本号  
                ObjectId preId= repository.resolve(preVision);//再次分解  
                RevCommit revTree = walk.parseCommit(preId);//反回树子树/文件的引用。
                return revTree;
            }else {
            	return revCommit;
            }
            
            
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {
        	repository.close();
        	walk.dispose();
        	walk.close();
        }
		return null;
    }  
}
