package eu.tjenwellens.pairtester.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import eu.tjenwellens.pairtester.R;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.http.util.EncodingUtils;

/**
 *
 * @author Tjen
 */
public class TCPClientActivity extends Activity {
    BufferedWriter output;
    Socket s = new Socket();

    //my handler
    private Handler mHandler;
    //where I want to display the message
    private TextView lblChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.tcpclient);

//        Button btnConnect = (Button)findViewById(R.id.btnConnect);
//        Button btnSend = (Button) findViewById(R.id.btnSend);
        //creating the TextView
//        lblChat = (TextView) findViewById(R.id.lblChat);
//        final EditText tbIP = (EditText) findViewById(R.id.tbIP);
//        final EditText tbInput = (EditText) findViewById(R.id.tbInput);

        //creating the handler
        mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                switch(msg.what)
                {
                    case 1:
                    {
                        lblChat.append("Received: " + msg.obj + "\r\n" );
                    }
                }
            }
        };
}

//Thread in which I receive incoming Messages from the server
    Thread communication = new Thread()
    {
        @Override
        public void run() 
        {
            String finalText = "";

            while(true) 
            {
                try
                {
                    DataInputStream inputStream = new DataInputStream(s.getInputStream());

                    int bytesRead;
                    byte[] b = new byte[4096];

                    bytesRead = inputStream.read(b,0,b.length);

                finalText = EncodingUtils.getAsciiString(b);

                //sending the message to the handler
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = finalText;

                    mHandler.handleMessage(msg);
            }

                //android.view.ViewRoot$CalledFromWrongThreadException: 
                //Only the original thread that created a view hierarchy can touch its views.
                //That's the exception I get when running the program
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
        }
    };

//method where I connect to the server
    private void connectToServer(String ip)
    {
        try
        {
            if(!ip.equals(""))
            {
                s = new Socket(ip, 3000);
            }

            else
            {
                s = new Socket("10.0.0.143", 3000);
            }

            output = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            output.write("Android Connected");
            output.flush();

    //      new CommuTask().execute();

            //starting the thread
            communication.start();
    //      s.close();
        }

        catch(UnknownHostException e)
        {
            e.printStackTrace();
            try {
                s.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        catch(IOException e)
        {
            e.printStackTrace();
            try {
                s.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}