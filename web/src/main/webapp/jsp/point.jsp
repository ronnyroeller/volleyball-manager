<%
String color = request.getParameter("color");

response.setContentType("image/jpeg");

com.sun.image.codec.jpeg.JPEGImageEncoder encoder = 
	com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(response.getOutputStream());

java.awt.image.BufferedImage bi =
	new java.awt.image.BufferedImage(1,1,java.awt.image.BufferedImage.TYPE_BYTE_INDEXED);

java.awt.Graphics2D graphics = bi.createGraphics();

int[] part = new int[3];
part[0] = Integer.parseInt(color.substring(1, 3), 16);
part[1] = Integer.parseInt(color.substring(3, 5), 16);
part[2] = Integer.parseInt(color.substring(5, 7), 16);
graphics.setColor(new java.awt.Color(part[0], part[1], part[2]));

graphics.fillRect(0, 0, 1, 1);

encoder.encode(bi);
%>