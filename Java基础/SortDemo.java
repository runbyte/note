// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SortDemo.java

import java.io.PrintStream;
import java.util.Arrays;

public class SortDemo
{

	public SortDemo()
	{
	}

	public static void main(String args[])
	{
		int ai[] = {
			12, 45, -78, 0, 3, -95, 48
		};
		System.out.println(Arrays.toString(ai));
		quickSort(0, ai.length - 1, ai);
		System.out.println("----after sort----");
		System.out.println(Arrays.toString(ai));
	}

	public static void bubbleSort(int ai[])
	{
		for (int i = 0; i < ai.length; i++)
		{
			for (int j = 0; j < ai.length - 1 - i; j++)
				if (ai[j] > ai[j + 1])
				{
					int k = ai[j];
					ai[j] = ai[j + 1];
					ai[j + 1] = k;
				}

		}

	}

	public static void insertSort(int ai[])
	{
		for (int i = 1; i < ai.length; i++)
		{
			int j = ai[i];
			int k;
			for (k = i - 1; k >= 0 && j < ai[k]; k--)
				ai[k + 1] = ai[k];

			ai[k + 1] = j;
		}

	}

	public static void quickSort(int i, int j, int ai[])
	{
		int k = i;
		int l = j;
		int i1 = ai[(i + j) / 2];
		boolean flag = false;
		do
		{
			if (k >= l)
				break;
			for (; ai[k] < i1; k++);
			for (; ai[l] > i1; l--);
			if (k >= l)
				break;
			int j1 = ai[k];
			ai[k] = ai[l];
			ai[l] = j1;
			if (ai[k] == i1)
				l--;
			if (ai[l] == i1)
				k++;
		} while (true);
		if (k == l)
		{
			k++;
			l--;
		}
		if (i < l)
			quickSort(i, l, ai);
		if (j > k)
			quickSort(k, j, ai);
	}
}
