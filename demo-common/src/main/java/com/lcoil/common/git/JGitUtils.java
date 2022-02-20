package com.lcoil.common.git;

import com.google.common.collect.Lists;
import com.lcoil.common.constant.CommonConst;
import com.lcoil.common.exception.base.BaseException;
import com.lcoil.common.git.params.GitParams;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.diff.RenameDetector;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * git操作工具类
 * @author  l-coil
 */
public class JGitUtils {

    private static final Logger logger = LoggerFactory.getLogger(JGitUtils.class);

    /**
     * 判断 本地是否存在 相关commitId 的分支，若有直接 切换分支
     *
     * @param gitPath git路径
     * @param commitId 提交id
     * @return
     */
    public static Boolean containBreach(String gitPath, String commitId) {
        boolean result = false;
        try {
            Git git = Git.open(new File(gitPath));
            List<Ref> list = git.branchList().call();
            for (Ref ref : list) {
                String breachName = ref.getName().substring(ref.getName().lastIndexOf("/") + 1);
                if (StringUtils.equals(breachName, commitId)) {
                    git.checkout().setName(commitId).call();
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            result = false;
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
     * 判断git 信息中是否包含此commit信息 若包含 则 基于commitId 创建分支 并切换分支
     *
     * @param gitPath
     * @param commitId
     * @return
     */
    public static Boolean containCommitId(String gitPath, String commitId) {
        boolean result = false;
        try {
            Git git = Git.open(new File(gitPath));
            Iterable<RevCommit> logIterable = git.log().all().call();
            List<RevCommit> commitList = Lists.newArrayList(logIterable.iterator());
            List<String> commitIdList = commitList.stream().map(RevCommit::getName).collect(Collectors.toList());
            result = commitIdList.contains(commitId);
            if (result) {
                // 根据commitId 拉取新分支，并且 以commitId 作为分支名称
                git.checkout().setName(commitId).call();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
     * 根据文件名级行号 获取 代码提交人信息
     *
     * @param filePath 文件路径
     * @param index 行号
     * @param codePath 代码路径
     * @param gitPath git路径
     * @return  PersonIdent
     */
    public static PersonIdent getAuthorByFilePathAndNum(String filePath, Integer index, String codePath, String gitPath){
        PersonIdent personIdent = null;
        String temp = gitPath.substring(gitPath.indexOf(CommonConst.WORK_PATH) + CommonConst.WORK_PATH.length());
        temp = temp.substring(temp.indexOf(codePath) + codePath.length());
        filePath = filePath.substring(filePath.indexOf(temp) + temp.length());
        try {
            Git git = Git.open(new File(gitPath));
            BlameResult blameResult = git.blame().setFilePath(filePath)
                    .setTextComparator(RawTextComparator.WS_IGNORE_ALL).call();
            if (null != blameResult) {
                personIdent = blameResult.getSourceAuthor(index);
                final RevCommit sourceCommit = blameResult.getSourceCommit(index);
                String commitInfo =sourceCommit != null ? "/" + sourceCommit.getCommitTime() + "/" + sourceCommit.getName() : "";
                logger.info("{}/{}:{}",personIdent.getName(),commitInfo,blameResult.getResultContents().getString(index));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return personIdent;

    }

    /**
     * 初始化一个GIT对象
     */
    public void initGit(GitParams gitParams, String codePath) throws Exception {
        // 如果目录存在就先更新后open
        Git git = null;
        try {
            if (new File(codePath).exists()) {
                git = Git.open(new File(codePath));
                // 如果已经存在该库，就使用fetch刷新一下
                FetchResult result = git.fetch()
                        //远程登录git的凭证
                        .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitParams.getUserName(), gitParams.getPassWord()))
                        .setCheckFetchedObjects(true)
                        .call();
                if (result == null) {
                    logger.error("获取的Git对象已失效");
                    throw new Exception("获取的Git对象已失效");
                }
                //如果分支在本地已存在，直接checkout即可。
                if (branchNameExist(git, gitParams.getBranch())) {
                    git.checkout().setCreateBranch(false).setName(gitParams.getBranch()).call();
                } else {
                    //如果分支在本地不存在，需要创建这个分支，并追踪到远程分支上面。
                    git.checkout().setCreateBranch(true).setName(gitParams.getBranch()).setStartPoint("origin/" + gitParams.getBranch()).call();
                }
                //拉取最新的提交
                git.pull().setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitParams.getUserName(), gitParams.getPassWord())).call();
                logger.info("breach:" + git.getRepository().getBranch());
            } else {
                // 如果不存在就clone
                git = Git.cloneRepository().setURI(gitParams.getGitUrl())
                        .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitParams.getUserName(), gitParams.getPassWord()))
                        //获取所有分支
                        .setCloneAllBranches(true)
                        //指定本地clone库
                        .setDirectory(new File(codePath))
                        .call();
                // 查看所clone下的项目的所有分支
                int c = 0;
                List<Ref> call = git.branchList().call();
                for (Ref ref : call) {
                    logger.info("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
                    c++;
                }
                logger.info("Number of branches: " + c);
            }
        } finally {
            if (git != null) {
                git.close();
            }
        }
    }

    /**
     * 获取指定分支代码
     *
     * @param gitParams git参数
     */
    public void getSpecifiedBranch(GitParams gitParams, String codePath) throws Exception {
        // 如果clone的仓库存在则删除
        boolean exists = new File(codePath).exists();
        if (exists) {

        }
        Git git = null;
        try {
            git = Git.cloneRepository().setURI(gitParams.getGitUrl())
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitParams.getUserName(), gitParams.getPassWord()))
                    //获取指定分支
                    .setBranch(gitParams.getBranch())
                    //指定本地clone库
                    .setDirectory(new File(codePath))
                    .call();
        } finally {
            if (git != null) {
                git.close();
            }
        }

    }

    /**
     * 获取所有的版本号与版本号对应的时间戳
     */
    public ArrayList<HashMap<String, Object>> getGitVersion(GitParams gitParams, String codePath)
            throws NoHeadException, GitAPIException, BaseException {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        Git git = null;
        try {
            git = Git.open(new File(codePath));
            Iterable<RevCommit> logIterable = git.log().call();
            // 获取所有版本号的迭代器
            Iterator<RevCommit> logIterator = logIterable.iterator();

            if (logIterator == null) {
                throw new BaseException("该项目暂无日志记录");
            }
            int row = 0;

            while (logIterator.hasNext()) {
                HashMap<String, Object> map = new HashMap<>();
                RevCommit commit = logIterator.next();
                //提交时间
                Date commitDate = commit.getAuthorIdent().getWhen();
                //提交人
                String commitPerson = commit.getAuthorIdent().getName();
                //提交的版本号（之后根据这个版本号去获取对应的详情记录）
                String commitID = commit.getName();
                map.put("version", commitID);
                map.put("commitDate", commitDate);
                map.put("commitPerson", commitPerson);
                list.add(row, map);
                row++;
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        } finally {
            if (git != null) {
                git.close();
            }
        }
        return list;
    }

    /**
     * 根据指定版本号获取版本号下面的详情记录
     *
     * @param revision
     * @return
     * @throws Exception
     */
    public HashMap<String, Object> getCommitLogList(String revision, GitParams gitParams, String codePath) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        // 通过git获取项目库
        Git git = Git.open(new File(codePath));
        Repository repository = git.getRepository();
        // 根据所需要查询的版本号查新ObjectId
        ObjectId objId = repository.resolve(revision);
        //根据版本号获取指定的详细记录
        Iterable<RevCommit> allCommitsLater = git.log().add(objId).call();
        if (allCommitsLater == null) {
            throw new Exception("该提交版本下无当前查询用户的提交记录");
        }
        Iterator<RevCommit> iter = allCommitsLater.iterator();
        //提交对象
        RevCommit commit = iter.next();
        //提交时间
        Date commitDate = commit.getAuthorIdent().getWhen();
        map.put("commitDate", commitDate);
        //提交人
        String commitPerson = commit.getAuthorIdent().getName();
        map.put("commitPerson", commitPerson);
        //提交日志
        String message = commit.getFullMessage();
        map.put("message", message);

        // 给存储库创建一个树的遍历器
        TreeWalk tw = new TreeWalk(repository);
        // 将当前commit的树放入树的遍历器中
        tw.addTree(commit.getTree());

        commit = iter.next();
        if (commit != null) {
            tw.addTree(commit.getTree());
        } else {
            return null;
        }

        tw.setRecursive(true);
        RenameDetector rd = new RenameDetector(repository);
        rd.addAll(DiffEntry.scan(tw));
        //获取到详细记录结果集    在这里就能获取到一个版本号对应的所有提交记录（详情！！！）
        List<DiffEntry> list = rd.compute();
        map.put("list", list);
        return map;
    }

    /**
     * 判断 本地是否存在此分支
     *
     * @param git
     * @param branchName
     * @return
     * @throws GitAPIException
     */
    public boolean branchNameExist(Git git, String branchName) throws GitAPIException {
        List<Ref> refs = git.branchList().call();
        for (Ref ref : refs) {
            if (ref.getName().contains(branchName)) {
                return true;
            }
        }
        return false;
    }

    public static String getCurrentTimeMillSecString() {
        // 获取时区“东八区”
        TimeZone zone = TimeZone.getTimeZone("GMT+8");
        SimpleDateFormat dfCurr = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        // 设置时区,使得输出时间与现在所处地区时间相符
        dfCurr.setTimeZone(zone);
        Date date = new Date();
        return dfCurr.format(date);
    }


}

