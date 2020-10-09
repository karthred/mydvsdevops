import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.framework.ui.UIUtil;

import matrix.db.Context;
import matrix.util.StringList;

public class JavaMailUtil_mxJPO {
	
	/**
	 * Utility method to send simple Mail
	 * @param context
	 * @param args
	 * @throws Exception
	 */
	
	public static void sendEmail(Context context,String[] args) throws Exception {

		try
		{  
			String strCurrentContext = context.getUser();
			String fromEmail = MqlUtil.mqlCommand(context,"print person \""+strCurrentContext+"\" select email dump");
			String fromEmailLabel = MqlUtil.mqlCommand(context,"print person \""+strCurrentContext+"\" select fullname dump");
			StringList attJavaMailRanges = FrameworkUtil.getRanges(context, "Matrix_JavaMail");
			String strRange = null;
			String strSMTPHost = null;
			String strSMTPPort = null;
			for (Object object : attJavaMailRanges) {
				strRange = object.toString();
				System.out.println("strRange---->"+strRange);
				String[] strArrTemp = strRange.split(":");
				//hmJavaMailConfig.put(strArrTemp[0].trim(), strArrTemp[1].trim());
				if("SMTP_HOST".equals(strArrTemp[0].trim())) {
					strSMTPHost = strArrTemp[1].trim();
				} else if("SMTP_PORT".equals(strArrTemp[0].trim())) {
					strSMTPPort = strArrTemp[1].trim();
				}
				
			}
			String toEmail = args[0];
			String ccEmail = args[1];
			String subject = args[2];
			String body = args[3];
			System.out.println("body---->"+body);
			//Get the session object  
			Properties properties = System.getProperties();  
			properties.setProperty("mail.smtp.host", strSMTPHost);
			properties.setProperty("mail.smtp.port", strSMTPPort);
			/*Session session = Session.getInstance(properties,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromAdmin, fromAdminPass);
				}
			});*/
			Session session = Session.getInstance(properties);
			//compose the message  
			MimeMessage message = new MimeMessage(session);  
			message.addHeader("Content-type", "text/HTML; charset=UTF-8");
			message.addHeader("format", "flowed");
			message.addHeader("Content-Transfer-Encoding", "8bit");
			message.setFrom(new InternetAddress(fromEmail,fromEmailLabel));  
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(toEmail));
			System.out.println("------1--------");
			System.out.println("------2--------");
			System.out.println("------3--------");
			if(UIUtil.isNotNullAndNotEmpty(ccEmail)) {
				message.addRecipient(Message.RecipientType.CC,new InternetAddress(ccEmail));
			}
			message.setSubject(subject); 
			message.setText(body);  
			// Send message
			Transport transport = session.getTransport("smtp");
			transport.connect();
			//transport.connect(host, iSmtpPort, fromAdmin, fromAdminPass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

			System.out.println("message sent successfully....");  

		} catch (Exception ex) {
			throw ex;
		} 
		System.out.println("Final Code=======");
		}  

	}
}
