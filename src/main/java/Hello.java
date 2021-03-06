import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

public class Hello implements RequestHandler<SQSEvent, Void> {
	@Override
	public Void handleRequest(SQSEvent event, Context context) {

		if (event != null) {

			for (SQSMessage msg : event.getRecords()) {
				System.out.println(new String(msg.getBody()));
			}
		}
		try {
			makeRequest();
		} catch (IOException e) {
			throw new RuntimeException();
		}

		return null;
	}

	private void makeRequest() throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		try {

//			HttpGet request = new HttpGet("https://regis.free.beeceptor.com");
			HttpPost request = new HttpPost("https://regis.free.beeceptor.com/teste");

			CloseableHttpResponse response = httpClient.execute(request);

			try {

				// Get HttpResponse Status
				System.out.println(response.getProtocolVersion()); // HTTP/1.1
				System.out.println(response.getStatusLine().getStatusCode()); // 200
				System.out.println(response.getStatusLine().getReasonPhrase()); // OK
				System.out.println(response.getStatusLine().toString()); // HTTP/1.1 200 OK

				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// return it as a String
					String result = EntityUtils.toString(entity);
					System.out.println(result);
				}

			} finally {
				response.close();
			}
		} finally {
			httpClient.close();
		}

	}

	public static void main(String[] args) throws IOException {
		new Hello().makeRequest();
	}
}