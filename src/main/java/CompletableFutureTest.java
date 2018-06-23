import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CompletableFutureTest {

    public static void main(String[] args) {

//        String str1 = "hogehogehogehoge";
//        String str2 = "hehehehehehehehe";
//        CompletableFuture<String> completableFutureA =
//                CompletableFuture.supplyAsync(() -> {
//                    // 処理A (並列処理させたい内容
//                    System.out.println("str1:" + str1.substring(3));
//                    return str1.substring(3);
//                });
//
//        CompletableFuture<String> completableFutureB =
//                CompletableFuture.supplyAsync(() -> {
//                    // 処理B (並列処理させたい内容
//                    return str2.replace("e", "i");
//                });
//
//        try{
//            System.out.println(completableFutureA.get()+completableFutureB.get());
//        }catch (InterruptedException | ExecutionException ee){
//            ee.printStackTrace();
//        }
        //とりあえず普通にソート
//        List<String> list_str = Arrays.asList("Hello", "My", "Name", "Is", "Atsutomo", "Tabata");
//        list_str.sort(Comparator.naturalOrder());
//        System.out.println(list_str);

        //ネストのあるリストを作って再帰的にソートする
        //リスト作成
        //!!!!!asListでリストを作ると固定長になるからnewしてaddAllを使う!!!!!
//        ArrayList<String> list_str1 = new ArrayList<>();
//        list_str1.addAll(Arrays.asList("Hello", "My", "Name", "Is", "Atsutomo", "Tabata"));
//        ArrayList<String> list_str2 = new ArrayList<>();
//        list_str2.addAll(Arrays.asList("Good", "bye", "See", "You", "Later"));
//        ArrayList<String> list_str3 = new ArrayList<>();
//        list_str3.addAll(Arrays.asList("Now", "There", "Are", "No", "Neta", "Orz"));
//        ArrayList<ArrayList<String>> list_parent = new ArrayList<>();
//        ArrayList<String> list_child1 = new ArrayList<>();
//        ArrayList<String> list_child2 = new ArrayList<>();
//        list_child1.add("A");
//        list_child2.addAll(Arrays.asList("C", "B"));
//        list_parent.addAll(Arrays.asList(list_child1, list_child2));
        //デバッグ用
//        list_str1.stream().forEach(s -> System.out.print(s + " "));
//        System.out.println();
//        list_str2.stream().forEach(s -> System.out.print(s + " "));
//        System.out.println();
//        list_str3.stream().forEach(s -> System.out.print(s + " "));
//        System.out.println();
//        list_parent.stream().forEach(s -> System.out.print(s + " "));
//        System.out.println();
        //ここまで
        //リスト合成
        //[[A], list_str1, [B, list_str2, C], list_str3]
        //にするつもり
//        list_parent.add(1, list_str1);
//        list_parent.get(2).add(1, list_str2.toString());
//        list_parent.add(3, list_str3);
//        System.out.println(list_parent);
//        for (int i = 0; i < list_parent.size(); i++) {
//            System.out.println("before sort: "+list_parent.get(i));
//            list_parent.get(i).sort(Comparator.naturalOrder());
//            System.out.println("after sort: "+list_parent.get(i));
//        }
//        for(List<String> list: list_parent){
//
//        }
//
//        System.out.println(list_parent);
        //10,000,000以上はやばいからサーバー上でやりましょう
        ArrayList<ArrayList> list = new ArrayList<>();
        int j = 1;
        int k = 0;
        Random rnd = new Random();
//        System.out.println("DO!!");
//        int tuples_num = Integer.parseInt(args[0]);
        int tuples_num = 50;
//        System.out.println(tuples_num);
        for(int i = 0; i < tuples_num; i++){
            ArrayList tmp = new ArrayList();
            int random = rnd.nextInt(10) + 1;
            if(i % 25 == 0 && i != 0){
                j++;
            }
            if(i % 5 == 0 && i != 0){
                k++;
            }
            if(i % 2 == 0){
                tmp.add("male");
                tmp.add("genre"+j);
                tmp.add("item"+k);
                tmp.add(random);
            }else{
                tmp.add("female");
                tmp.add("genre"+j);
                tmp.add("item"+k);
                tmp.add(random);
            }
            list.add(tmp);
        }
        System.out.println("Tuple size: "+tuples_num);
//        System.out.println(list);
        Hashtable result = new Hashtable();
        double start= System.currentTimeMillis();
        result = recursiveSort(list);
        double end = System.currentTimeMillis();
//        System.out.println(result);
        System.out.println("Time taken: " + (end - start) + "ms");
        System.out.println();
    }
    private static Hashtable recursiveSort(ArrayList listlist){
        System.out.println("Into rec Thread-"+Thread.currentThread().getName());
        boolean last_flag = false;
        Hashtable buf = new Hashtable();
        System.out.println("listlist: "+listlist);
//        System.out.println("*****in to rec*****");
//        System.out.println("listlist: "+listlist);
        for(Object list: listlist){
            ArrayList alist = (ArrayList)list;
//            System.out.println("alist: "+alist);
            ArrayList key = new ArrayList();
//            System.out.println("key_specified: "+key);
            ArrayList value = new ArrayList();
//            System.out.println("value: "+value);
            key.add(alist.get(0).toString());
            ArrayList tmp = new ArrayList();
            for(int i = 1; i < alist.size(); i++){
                tmp.add(alist.get(i));
            }
//            System.out.println("key_added: "+key);
            if(buf.containsKey(key)){
//                System.out.println("-----into contain-----");
                value = (ArrayList)buf.get(key);
                value.add(tmp);
//                System.out.println("value: "+value);
            }else{
//                System.out.println("-----into not contain-----");
                value.add(tmp);
//                System.out.println("value: "+value);
            }
            buf.put(key, value);
//            System.out.println("buf_putted: "+buf);
            if(((ArrayList)value.get(0)).size() == 1){
//                System.out.println("-----into last-----");
                last_flag = true;
            }
        }
        if(!last_flag){
//            System.out.println("-----into last phase-----");
            Enumeration keys = buf.keys();
//            //並列じゃない場合
//            while(keys.hasMoreElements()){
////                System.out.println("-----into while-----");
//                ArrayList value_in = new ArrayList();
//                Object key = keys.nextElement();
////                System.out.println("key: "+key);
//                value_in = (ArrayList)buf.get(key);
////                System.out.println("value_in: "+value_in);
//                Hashtable ret = recursiveSort(value_in);
////                System.out.println("ret: "+ret);
//                buf.put(key, ret);
////                System.out.println("buf_while_putted: "+buf);
//            }
            //ここまで
            //並列の場合
            List<CompletableFuture> futurelist = new ArrayList<>();
            while(keys.hasMoreElements()){
                Object key = keys.nextElement();
                final ArrayList value_in = (ArrayList)buf.get(key);
                Supplier<ArrayList> sup = () -> value_in;
                Function<ArrayList, Hashtable> fun = v -> recursiveSort(v);
                Consumer<Hashtable> con = v -> buf.put(key, v);
                System.out.println(value_in);
                CompletableFuture<Void> tmp_future = CompletableFuture.supplyAsync(sup).thenApplyAsync(fun).thenAccept(con);
                futurelist.add(tmp_future);
            }
            CompletableFuture.allOf(
                    futurelist.toArray(new CompletableFuture[futurelist.size()])
            ).join();
            //ここまで
//            System.out.println("-----out while-----");
//            System.out.println("buf_putted_last: "+buf);
//            System.out.println("*****out rec*****");
            return buf;
        }else{
//            System.out.println("buf_putted_not_last: "+buf);
//            System.out.println("*****out rec*****");
            return buf;
        }
    }


}
