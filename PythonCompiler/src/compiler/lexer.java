import java.util.*;
import java.io.*;
/* semantic value of token returned by scanner */
public class lexer
{
	BufferedReader reader;
	Yylex yy;
	public lexer(String filein) throws FileNotFoundException 
	{
		reader = new BufferedReader(new FileReader(filein));
		yy = new Yylex (reader);
	}
	public String getNextToken(String filein) throws IOException
	{
		return yy.next_token();
	}
	public static void main (String[] args) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader("src/compiler/ttt.py"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("testout.txt"));
		Yylex yy = new Yylex (reader);
		while(true)
		{
			String x =yy.next_token();
			if(x==null)
				break;
			writer.write(x);	
			writer.write('\n');
		}
		reader.close();
		writer.close();
	}
}


class Yylex {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NOT_ACCEPT,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NOT_ACCEPT,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NOT_ACCEPT,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NOT_ACCEPT,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NOT_ACCEPT,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"3:8,4:2,1,3,4:2,3:18,4,23,42,3:2,21,3,43,37:2,20,18,37,19,40,2,39:10,37:2,2" +
"4,22,24,3,37,38:4,41,9,38:7,25,38:5,5,38:6,44,3,45,3,38,3,10,26,28,14,8,29," +
"33,36,15,38,27,11,34,13,16,31,38,6,12,17,7,38,35,30,32,38,37,3,37,3:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,111,
"0,1,2,3,1,4,1,5,6,7,1:3,8,9,10,11,12,13,14,15,16,1,17,18,19,20,16,21,22,9,1" +
"9,23,15,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,9,40,41,42,43,9,44," +
"45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69," +
"70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,9,86,87,88,89,90,91,92,93,5" +
"3,94,95,96")[0];

	private int yy_nxt[][] = unpackFromString(97,46,
"1,2,3,4,2,5,92,98,101,102,18,103,98,49,51,24,28,52,16,22,26,17,16,23,22,104" +
",105,98,106,53,98,107,108,109,98,110,98,6,98,7,6,98,27,31,19,6,-1:47,2,-1:2" +
",2,-1:43,8,-1:48,98,54,98:11,-1:7,98:12,-1,98,55,-1,98,-1:43,7,15,-1:7,8:44" +
",-1:5,98:7,100,98:5,-1:7,98:12,-1,98,55,-1,98,-1:43,13,-1,29,-1:9,98:13,-1:" +
"7,98:12,-1,98,55,-1,98,-1:43,13,-1:28,6,-1:42,22,-1:31,98:7,9,32,98:4,-1:7," +
"98:12,-1,98,55,-1,98,-1:49,12,-1:39,20,-1:7,21:41,10,21:3,-1:22,22,-1:28,98" +
":7,30:2,98:4,-1:7,98:4,50,98:4,62,98:2,-1,98,55,-1,98,-1:5,25:42,11,25:2,-1" +
":20,22,-1:30,98,30,98:11,-1:7,98:12,-1,98,55,-1,98,-1:22,33:2,-1:19,20,-1:1" +
"1,98:9,30,98:3,-1:7,98:12,-1,98,55,-1,98,-1:9,98:8,74,98:3,30,-1:7,98:12,-1" +
",98,55,-1,98,-1:9,98:6,50,98:6,-1:7,98:4,50,98:7,-1,98,55,-1,98,-1:9,98:13," +
"-1:7,98:7,50,98:4,-1,98,55,-1,98,-1:9,98,50,98:11,-1:7,98:12,-1,98,55,-1,98" +
",-1:9,98:3,14,98:9,-1:7,98:12,-1,98,55,-1,98,-1:9,98:3,50,98:9,-1:7,98:12,-" +
"1,98,55,-1,98,-1:9,98:13,-1:7,98:4,50,98:7,-1,98,55,-1,98,-1:9,98:7,50,98:5" +
",-1:7,98:12,-1,98,55,-1,98,-1:9,98:13,-1:7,98:11,50,-1,98,55,-1,98,-1:9,98:" +
"13,-1:7,98:2,50,98:9,-1,98,55,-1,98,-1:9,98:9,50,98:3,-1:7,98:12,-1,98,55,-" +
"1,98,-1:9,98:8,50,98:4,-1:7,98:12,-1,98,55,-1,98,-1:9,98:12,50,-1:7,98:12,-" +
"1,98,55,-1,98,-1:9,98:5,50,98:7,-1:7,98:12,-1,98,55,-1,98,-1:9,98:6,50,98:6" +
",-1:7,98:12,-1,98,55,-1,98,-1:9,98:11,34,98,-1:7,98:12,-1,98,55,-1,98,-1:9," +
"98:3,35,98:9,-1:7,98:12,-1,98,55,-1,98,-1:9,98,36,98:11,-1:7,98:12,-1,98,55" +
",-1,98,-1:9,98:10,95,37,98,-1:7,98:12,-1,98,55,-1,98,-1:9,98:2,38,98:10,-1:" +
"7,98:12,-1,98,55,-1,98,-1:9,98:12,70,-1:7,98:12,-1,98,55,-1,98,-1:9,98:10,7" +
"1,98:2,-1:7,98:12,-1,98,55,-1,98,-1:9,98:7,39,98:2,40,98:2,-1:7,98:12,-1,98" +
",55,-1,98,-1:9,98:13,-1:7,98:3,94,98:8,-1,98,55,-1,98,-1:9,98:6,72,98:6,-1:" +
"7,98:12,-1,98,55,-1,98,-1:9,98:13,-1:7,98:9,73,98:2,-1,98,55,-1,98,-1:9,98:" +
"13,-1:7,98:6,96,98:5,-1,98,55,-1,98,-1:9,98:8,39,98:4,-1:7,98:12,-1,98,55,-" +
"1,98,-1:9,98:3,75,98:9,-1:7,98:12,-1,98,55,-1,98,-1:9,98:5,67,98:7,-1:7,98:" +
"12,-1,98,55,-1,98,-1:9,98:8,76,98:4,-1:7,98:12,-1,98,55,-1,98,-1:9,98:7,41," +
"98:5,-1:7,98:12,-1,98,55,-1,98,-1:9,98:11,97,98,-1:7,98:12,-1,98,55,-1,98,-" +
"1:9,98:12,42,-1:7,98:12,-1,98,55,-1,98,-1:9,98:2,80,98:10,-1:7,98:12,-1,98," +
"55,-1,98,-1:9,98:7,39,98:5,-1:7,98:12,-1,98,55,-1,98,-1:9,98:7,38,98:5,-1:7" +
",98:12,-1,98,55,-1,98,-1:9,98:13,-1:7,98,83,98:10,-1,98,55,-1,98,-1:9,98:6," +
"84,98:6,-1:7,98:12,-1,98,55,-1,98,-1:9,98:5,43,98:7,-1:7,98:12,-1,98,55,-1," +
"98,-1:9,98:12,85,-1:7,98:12,-1,98,55,-1,98,-1:9,98:5,86,98:7,-1:7,98:12,-1," +
"98,55,-1,98,-1:9,98:6,44,98:6,-1:7,98:12,-1,98,55,-1,98,-1:9,98:6,39,98:6,-" +
"1:7,98:12,-1,98,55,-1,98,-1:9,98,45,98:11,-1:7,98:12,-1,98,55,-1,98,-1:9,98" +
":13,-1:7,98:6,46,98:5,-1,98,55,-1,98,-1:9,98,46,98:11,-1:7,98:12,-1,98,55,-" +
"1,98,-1:9,98:9,47,98:3,-1:7,98:12,-1,98,55,-1,98,-1:9,98:11,88,98,-1:7,98:1" +
"2,-1,98,55,-1,98,-1:9,98:10,89,98:2,-1:7,98:12,-1,98,55,-1,98,-1:9,98:6,90," +
"98:6,-1:7,98:12,-1,98,55,-1,98,-1:9,98:5,48,98:7,-1:7,98:12,-1,98,55,-1,98," +
"-1:9,98:13,-1:7,98:3,87,98:8,-1,98,55,-1,98,-1:9,98:8,91,98:4,-1:7,98:12,-1" +
",98,55,-1,98,-1:9,98:6,36,98:6,-1:7,98:12,-1,98,55,-1,98,-1:9,98:2,39,98:10" +
",-1:7,98:12,-1,98,55,-1,98,-1:9,98:3,56,98,57,98:7,-1:7,98:12,-1,98,55,-1,9" +
"8,-1:9,98:10,78,98:2,-1:7,98:12,-1,98,55,-1,98,-1:9,98:3,81,98:9,-1:7,98:12" +
",-1,98,55,-1,98,-1:9,98:8,77,98:4,-1:7,98:12,-1,98,55,-1,98,-1:9,98:11,82,9" +
"8,-1:7,98:12,-1,98,55,-1,98,-1:9,98:13,-1:7,98,87,98:10,-1,98,55,-1,98,-1:9" +
",98:10,79,98:2,-1:7,98:12,-1,98,55,-1,98,-1:9,98:3,82,98:9,-1:7,98:12,-1,98" +
",55,-1,98,-1:9,98:6,58,98:6,-1:7,98:5,59,98:6,-1,98,55,-1,98,-1:9,98:5,60,9" +
"8:7,-1:7,98:12,-1,98,55,-1,98,-1:9,98:5,61,98:7,-1:7,98:12,-1,98,55,-1,98,-" +
"1:9,98:11,63,98,-1:7,98:12,-1,98,55,-1,98,-1:9,98,64,98:11,-1:7,98:12,-1,98" +
",55,-1,98,-1:9,98:6,65,98:4,66,98,-1:7,98:12,-1,98,55,-1,98,-1:9,98:3,93,98" +
":9,-1:7,98:12,-1,98,55,-1,98,-1:9,98:6,68,98:6,-1:7,98:12,-1,98,55,-1,98,-1" +
":9,98:10,69,98:2,-1:7,98:11,99,-1,98,55,-1,98,-1:4");

	public String next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

  return null;
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ }
					case -3:
						break;
					case 3:
						{ 
  return "OPERATOR\t"+yytext(); 
}
					case -4:
						break;
					case 4:
						{ 
  return "Undefined\t"+yytext(); 
}
					case -5:
						break;
					case 5:
						{ 
  return "ID\t"+yytext(); 
}
					case -6:
						break;
					case 6:
						{ 
  return "DELIMITER\t"+yytext(); 
}
					case -7:
						break;
					case 7:
						{ 
  return "INT\t"+yytext(); 
}
					case -8:
						break;
					case 8:
						{ }
					case -9:
						break;
					case 9:
						{ 
  return "KW\t"+yytext(); 
}
					case -10:
						break;
					case 10:
						{ 
  return "STRING\t"+yytext(); 
}
					case -11:
						break;
					case 11:
						{ 
  return "CH\t"+yytext(); 
}
					case -12:
						break;
					case 12:
						{ 
  return "AA\t[]"; 
}
					case -13:
						break;
					case 13:
						{ 
  return "DL\t"+yytext(); 
}
					case -14:
						break;
					case 14:
						{ 
  return "BOOL\t"+yytext(); 
}
					case -15:
						break;
					case 16:
						{ 
  return "OPERATOR\t"+yytext(); 
}
					case -16:
						break;
					case 17:
						{ 
  return "Undefined\t"+yytext(); 
}
					case -17:
						break;
					case 18:
						{ 
  return "ID\t"+yytext(); 
}
					case -18:
						break;
					case 19:
						{ 
  return "DELIMITER\t"+yytext(); 
}
					case -19:
						break;
					case 20:
						{ 
  return "DL\t"+yytext(); 
}
					case -20:
						break;
					case 22:
						{ 
  return "OPERATOR\t"+yytext(); 
}
					case -21:
						break;
					case 23:
						{ 
  return "Undefined\t"+yytext(); 
}
					case -22:
						break;
					case 24:
						{ 
  return "ID\t"+yytext(); 
}
					case -23:
						break;
					case 26:
						{ 
  return "OPERATOR\t"+yytext(); 
}
					case -24:
						break;
					case 27:
						{ 
  return "Undefined\t"+yytext(); 
}
					case -25:
						break;
					case 28:
						{ 
  return "ID\t"+yytext(); 
}
					case -26:
						break;
					case 30:
						{ 
  return "OPERATOR\t"+yytext(); 
}
					case -27:
						break;
					case 31:
						{ 
  return "Undefined\t"+yytext(); 
}
					case -28:
						break;
					case 32:
						{ 
  return "ID\t"+yytext(); 
}
					case -29:
						break;
					case 34:
						{ 
  return "ID\t"+yytext(); 
}
					case -30:
						break;
					case 35:
						{ 
  return "ID\t"+yytext(); 
}
					case -31:
						break;
					case 36:
						{ 
  return "ID\t"+yytext(); 
}
					case -32:
						break;
					case 37:
						{ 
  return "ID\t"+yytext(); 
}
					case -33:
						break;
					case 38:
						{ 
  return "ID\t"+yytext(); 
}
					case -34:
						break;
					case 39:
						{ 
  return "ID\t"+yytext(); 
}
					case -35:
						break;
					case 40:
						{ 
  return "ID\t"+yytext(); 
}
					case -36:
						break;
					case 41:
						{ 
  return "ID\t"+yytext(); 
}
					case -37:
						break;
					case 42:
						{ 
  return "ID\t"+yytext(); 
}
					case -38:
						break;
					case 43:
						{ 
  return "ID\t"+yytext(); 
}
					case -39:
						break;
					case 44:
						{ 
  return "ID\t"+yytext(); 
}
					case -40:
						break;
					case 45:
						{ 
  return "ID\t"+yytext(); 
}
					case -41:
						break;
					case 46:
						{ 
  return "ID\t"+yytext(); 
}
					case -42:
						break;
					case 47:
						{ 
  return "ID\t"+yytext(); 
}
					case -43:
						break;
					case 48:
						{ 
  return "ID\t"+yytext(); 
}
					case -44:
						break;
					case 49:
						{ 
  return "ID\t"+yytext(); 
}
					case -45:
						break;
					case 50:
						{ 
  return "KW\t"+yytext(); 
}
					case -46:
						break;
					case 51:
						{ 
  return "ID\t"+yytext(); 
}
					case -47:
						break;
					case 52:
						{ 
  return "ID\t"+yytext(); 
}
					case -48:
						break;
					case 53:
						{ 
  return "ID\t"+yytext(); 
}
					case -49:
						break;
					case 54:
						{ 
  return "ID\t"+yytext(); 
}
					case -50:
						break;
					case 55:
						{ 
  return "ID\t"+yytext(); 
}
					case -51:
						break;
					case 56:
						{ 
  return "ID\t"+yytext(); 
}
					case -52:
						break;
					case 57:
						{ 
  return "ID\t"+yytext(); 
}
					case -53:
						break;
					case 58:
						{ 
  return "ID\t"+yytext(); 
}
					case -54:
						break;
					case 59:
						{ 
  return "ID\t"+yytext(); 
}
					case -55:
						break;
					case 60:
						{ 
  return "ID\t"+yytext(); 
}
					case -56:
						break;
					case 61:
						{ 
  return "ID\t"+yytext(); 
}
					case -57:
						break;
					case 62:
						{ 
  return "ID\t"+yytext(); 
}
					case -58:
						break;
					case 63:
						{ 
  return "ID\t"+yytext(); 
}
					case -59:
						break;
					case 64:
						{ 
  return "ID\t"+yytext(); 
}
					case -60:
						break;
					case 65:
						{ 
  return "ID\t"+yytext(); 
}
					case -61:
						break;
					case 66:
						{ 
  return "ID\t"+yytext(); 
}
					case -62:
						break;
					case 67:
						{ 
  return "ID\t"+yytext(); 
}
					case -63:
						break;
					case 68:
						{ 
  return "ID\t"+yytext(); 
}
					case -64:
						break;
					case 69:
						{ 
  return "ID\t"+yytext(); 
}
					case -65:
						break;
					case 70:
						{ 
  return "ID\t"+yytext(); 
}
					case -66:
						break;
					case 71:
						{ 
  return "ID\t"+yytext(); 
}
					case -67:
						break;
					case 72:
						{ 
  return "ID\t"+yytext(); 
}
					case -68:
						break;
					case 73:
						{ 
  return "ID\t"+yytext(); 
}
					case -69:
						break;
					case 74:
						{ 
  return "ID\t"+yytext(); 
}
					case -70:
						break;
					case 75:
						{ 
  return "ID\t"+yytext(); 
}
					case -71:
						break;
					case 76:
						{ 
  return "ID\t"+yytext(); 
}
					case -72:
						break;
					case 77:
						{ 
  return "ID\t"+yytext(); 
}
					case -73:
						break;
					case 78:
						{ 
  return "ID\t"+yytext(); 
}
					case -74:
						break;
					case 79:
						{ 
  return "ID\t"+yytext(); 
}
					case -75:
						break;
					case 80:
						{ 
  return "ID\t"+yytext(); 
}
					case -76:
						break;
					case 81:
						{ 
  return "ID\t"+yytext(); 
}
					case -77:
						break;
					case 82:
						{ 
  return "ID\t"+yytext(); 
}
					case -78:
						break;
					case 83:
						{ 
  return "ID\t"+yytext(); 
}
					case -79:
						break;
					case 84:
						{ 
  return "ID\t"+yytext(); 
}
					case -80:
						break;
					case 85:
						{ 
  return "ID\t"+yytext(); 
}
					case -81:
						break;
					case 86:
						{ 
  return "ID\t"+yytext(); 
}
					case -82:
						break;
					case 87:
						{ 
  return "ID\t"+yytext(); 
}
					case -83:
						break;
					case 88:
						{ 
  return "ID\t"+yytext(); 
}
					case -84:
						break;
					case 89:
						{ 
  return "ID\t"+yytext(); 
}
					case -85:
						break;
					case 90:
						{ 
  return "ID\t"+yytext(); 
}
					case -86:
						break;
					case 91:
						{ 
  return "ID\t"+yytext(); 
}
					case -87:
						break;
					case 92:
						{ 
  return "ID\t"+yytext(); 
}
					case -88:
						break;
					case 93:
						{ 
  return "ID\t"+yytext(); 
}
					case -89:
						break;
					case 94:
						{ 
  return "ID\t"+yytext(); 
}
					case -90:
						break;
					case 95:
						{ 
  return "ID\t"+yytext(); 
}
					case -91:
						break;
					case 96:
						{ 
  return "ID\t"+yytext(); 
}
					case -92:
						break;
					case 97:
						{ 
  return "ID\t"+yytext(); 
}
					case -93:
						break;
					case 98:
						{ 
  return "ID\t"+yytext(); 
}
					case -94:
						break;
					case 99:
						{ 
  return "ID\t"+yytext(); 
}
					case -95:
						break;
					case 100:
						{ 
  return "ID\t"+yytext(); 
}
					case -96:
						break;
					case 101:
						{ 
  return "ID\t"+yytext(); 
}
					case -97:
						break;
					case 102:
						{ 
  return "ID\t"+yytext(); 
}
					case -98:
						break;
					case 103:
						{ 
  return "ID\t"+yytext(); 
}
					case -99:
						break;
					case 104:
						{ 
  return "ID\t"+yytext(); 
}
					case -100:
						break;
					case 105:
						{ 
  return "ID\t"+yytext(); 
}
					case -101:
						break;
					case 106:
						{ 
  return "ID\t"+yytext(); 
}
					case -102:
						break;
					case 107:
						{ 
  return "ID\t"+yytext(); 
}
					case -103:
						break;
					case 108:
						{ 
  return "ID\t"+yytext(); 
}
					case -104:
						break;
					case 109:
						{ 
  return "ID\t"+yytext(); 
}
					case -105:
						break;
					case 110:
						{ 
  return "ID\t"+yytext(); 
}
					case -106:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
