package com.student.mappic.addmap.common.myviews.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class JCircle {
	private FloatBuffer mVertexBuffer=null;
	private FloatBuffer mColorBuffer=null;
	private ByteBuffer mIndexBuffer=null;

	private List<Float> vertices=new ArrayList<>();
	private List<Float> colors=new ArrayList<>();
	private List<Byte> indices=new ArrayList<>();

	public JCircle(int verticesCount, float x, float y, float r, float ratio){
		vertices.add((float)x);
		vertices.add((float)y);
		vertices.add(-1.0f);

		colors.add(0.0f);
		colors.add(0.0f);
		colors.add(1.0f);
		colors.add(1.0f);

		for(int i=0;i<verticesCount;i++)
		{
			double theta=2.0f*Math.PI*i/verticesCount;
			double cx=x+r*Math.cos(theta);
			double cy=y+r*Math.sin(theta)*ratio;
			vertices.add((float)cx);
			vertices.add((float)cy);
			vertices.add(-1.0f);

			colors.add(0.0f);
			colors.add(0.0f);
			colors.add(1.0f);
			colors.add(1.0f);

		}

		for(int i=1;i<verticesCount;i++)
		{
			indices.add((byte) 0);
			indices.add((byte)i);
			indices.add((byte)(i+1));

		}
		indices.add((byte) 0);
		indices.add((byte)verticesCount);
		indices.add((byte)1);


		ByteBuffer buff=ByteBuffer.allocateDirect(vertices.size()*4);
		buff.order(ByteOrder.nativeOrder());
		mVertexBuffer=buff.asFloatBuffer();
		float[] floatArray=new float[vertices.size()];
		for(int i=0;i<vertices.size();i++)
		{
			floatArray[i]=vertices.get(i);
		}
		mVertexBuffer.put(floatArray);
		mVertexBuffer.position(0);


		buff=ByteBuffer.allocateDirect(colors.size()*4);
		buff.order(ByteOrder.nativeOrder());
		mColorBuffer=buff.asFloatBuffer();
		floatArray=new float[colors.size()];
		for(int i=0;i<colors.size();i++)
		{
			floatArray[i]=colors.get(i);
		}
		mColorBuffer.put(floatArray);
		mColorBuffer.position(0);


		buff=ByteBuffer.allocateDirect(indices.size());
		buff.order(ByteOrder.nativeOrder());
		mIndexBuffer=buff;

		byte[] byteArray=new byte[indices.size()];
		for(int i=0;i<indices.size();i++)
		{
			byteArray[i]=indices.get(i);
		}
		mIndexBuffer.put(byteArray);
		mIndexBuffer.position(0);


	}
	public void draw(GL10 gl10){
		gl10.glFrontFace(GL10.GL_CW);

		gl10.glVertexPointer(3,GL10.GL_FLOAT,0,mVertexBuffer);
		gl10.glColorPointer(4,GL10.GL_FLOAT,0,mColorBuffer);

		gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl10.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl10.glDrawElements(GL10.GL_TRIANGLES,indices.size(),GL10.GL_UNSIGNED_BYTE,mIndexBuffer);

		gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl10.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}
}
