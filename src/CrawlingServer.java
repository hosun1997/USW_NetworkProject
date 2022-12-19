import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 검색한 영화에 대한 정보를 받아 결과를 제공해줍니다.
 */
public class CrawlingServer {
    public CrawlingServer() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("서버 가동됨");
            while (true) {
                System.out.println("Waiting Client...");
                socket = serverSocket.accept();

                CrawlingThread crawlingThread = new CrawlingThread(socket);
                crawlingThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                serverSocket.close();
                System.out.println("Sever Closed!");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("SeverSocket Communication Error!");
            }
        }
    }

    class CrawlingThread extends Thread{
        ObjectInputStream ois;
        ObjectOutputStream oos;
        InputStream is;
        public CrawlingThread(Socket socket){

            is = null;
            try {
                is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                ois = new ObjectInputStream(is);
                oos = new ObjectOutputStream(os);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        @Override
        public void run(){
            String search_title;
            try {
                search_title = (String) ois.readObject();//Client가 검색한 영화

                Thread thread = new Thread(new webCrawling(search_title));
                thread.start();
                thread.join();

                MovieObj movie = new MovieObj();
                movie.setMovie_title(webCrawling.movie_title);
                movie.setScore_adc(webCrawling.score_adc);
                movie.setScore_spec(webCrawling.score_spec);
                movie.setScore_ntz(webCrawling.score_ntz);
                movie.setSummary(webCrawling.summary);
                movie.setPoster(webCrawling.poster);
                movie.setReview(webCrawling.review);
                movie.setReview_score(webCrawling.review_score);
                movie.setReview_reple(webCrawling.review_reple);
                movie.setReview_user(webCrawling.review_user);
                movie.setReview_date(webCrawling.review_date);

                oos.writeObject(movie);//검색한 영화에 대한 정보 제공
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        new CrawlingServer();
    }

}
