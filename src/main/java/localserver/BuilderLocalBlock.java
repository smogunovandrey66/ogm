package localserver;

public class BuilderLocalBlock {
    private static LocalBlock curLocalBlock = null;

    public static LocalBlock startBuild(){
        curLocalBlock = new LocalBlock();
        return curLocalBlock;
    }

    public static LocalBlock buildRootPath(String rootPath){

       return curLocalBlock;
    }

    public static LocalBlock buildId(byte id){
        curLocalBlock.setId(id);
        return curLocalBlock;
    }

    public static LocalBlock buildStrInfo(String ... infoItems){
        curLocalBlock.getStrInfo().clear();
        for(String item: infoItems){
            curLocalBlock.getStrInfo().add(item);
        }
        return curLocalBlock;
    }

    public static LocalBlock start(){
        LocalBlock temp = curLocalBlock;
        curLocalBlock = null;
        curLocalBlock.start();
        return temp;
    }
}
