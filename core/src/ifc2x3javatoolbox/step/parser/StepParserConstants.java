/* Generated By: IFC Tools Project EXPRESS TO JAVA COMPILER: Do not edit this file!! */
/* Generated By:JavaCC: Do not edit this line. StepParserConstants.java */
package ifc2x3javatoolbox.step.parser;
/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 *<br><br>
 * Copyright: CC BY-NC-SA 3.0 DE (cc) 2013 Eike Tauscher and Michael Theiler<br><br>
 * The whole package including this class is licensed under<br>
 * <a rel='license' href='http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en/'>
 * Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Germany</a>.<br><br>
 * If you are using the package or parts of it in any commercial way, a commercial license is required. 
 * Visit <a href='http://www.ifctoolsproject.com'>http://www.ifctoolsproject.com</a> for more information
 * or contact us directly: <a href='mailto:info@ifctoolsproject.com'>info@ifctoolsproject.com</a><br>
 */
public interface StepParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int SPACE = 1;
  /** RegularExpression Id. */
  int CARRIAGE_RETURN = 3;
  /** RegularExpression Id. */
  int COMMENT_START = 5;
  /** RegularExpression Id. */
  int EOL = 6;
  /** RegularExpression Id. */
  int END_COMMENT = 7;
  /** RegularExpression Id. */
  int ANYLETTER = 8;
  /** RegularExpression Id. */
  int LPAREN = 9;
  /** RegularExpression Id. */
  int RPAREN = 10;
  /** RegularExpression Id. */
  int LBRACE = 11;
  /** RegularExpression Id. */
  int RBRACE = 12;
  /** RegularExpression Id. */
  int LBRACKET = 13;
  /** RegularExpression Id. */
  int RBRACKET = 14;
  /** RegularExpression Id. */
  int SEMICOLON = 15;
  /** RegularExpression Id. */
  int COLON = 16;
  /** RegularExpression Id. */
  int COMMA = 17;
  /** RegularExpression Id. */
  int DOT = 18;
  /** RegularExpression Id. */
  int EQ = 19;
  /** RegularExpression Id. */
  int DOLLAR = 20;
  /** RegularExpression Id. */
  int STAR = 21;
  /** RegularExpression Id. */
  int SLASH = 22;
  /** RegularExpression Id. */
  int ENDSCOPE = 23;
  /** RegularExpression Id. */
  int USER_DEFINED_KEYWORD = 24;
  /** RegularExpression Id. */
  int STANDARD_KEYWORD = 25;
  /** RegularExpression Id. */
  int SIGN = 26;
  /** RegularExpression Id. */
  int INTEGER = 27;
  /** RegularExpression Id. */
  int REAL = 28;
  /** RegularExpression Id. */
  int NON_Q_CHAR = 29;
  /** RegularExpression Id. */
  int STRING = 30;
  /** RegularExpression Id. */
  int ENTITY_INSTANCE_NAME = 31;
  /** RegularExpression Id. */
  int ENUMERATION = 32;
  /** RegularExpression Id. */
  int HEX = 33;
  /** RegularExpression Id. */
  int BINARY = 34;
  /** RegularExpression Id. */
  int DIGIT = 35;
  /** RegularExpression Id. */
  int LOWER = 36;
  /** RegularExpression Id. */
  int UPPER = 37;
  /** RegularExpression Id. */
  int SPECIAL = 38;
  /** RegularExpression Id. */
  int REVERSE_SOLIDUS = 39;
  /** RegularExpression Id. */
  int APOSTROPHE = 40;
  /** RegularExpression Id. */
  int CHARACTER = 41;
  /** RegularExpression Id. */
  int CONTROL_DIRECTIVE = 42;
  /** RegularExpression Id. */
  int PAGE = 43;
  /** RegularExpression Id. */
  int ALPHABET = 44;
  /** RegularExpression Id. */
  int EXTENDED2 = 45;
  /** RegularExpression Id. */
  int EXTENDED4 = 46;
  /** RegularExpression Id. */
  int END_EXTENDED = 47;
  /** RegularExpression Id. */
  int ARBITRARY = 48;
  /** RegularExpression Id. */
  int HEX_ONE = 49;
  /** RegularExpression Id. */
  int HEX_TWO = 50;
  /** RegularExpression Id. */
  int HEX_FOUR = 51;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int WITHIN_COMMENT = 1;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\r\"",
    "\"\\f\"",
    "\"/*\"",
    "\"\\n\"",
    "\"*/\"",
    "<ANYLETTER>",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\":\"",
    "\",\"",
    "\".\"",
    "\"=\"",
    "\"$\"",
    "\"*\"",
    "\"/\"",
    "\"ENDSCOPE\"",
    "<USER_DEFINED_KEYWORD>",
    "<STANDARD_KEYWORD>",
    "<SIGN>",
    "<INTEGER>",
    "<REAL>",
    "<NON_Q_CHAR>",
    "<STRING>",
    "<ENTITY_INSTANCE_NAME>",
    "<ENUMERATION>",
    "<HEX>",
    "<BINARY>",
    "<DIGIT>",
    "<LOWER>",
    "<UPPER>",
    "<SPECIAL>",
    "\"\\\\\"",
    "\"\\\'\"",
    "<CHARACTER>",
    "<CONTROL_DIRECTIVE>",
    "<PAGE>",
    "<ALPHABET>",
    "<EXTENDED2>",
    "<EXTENDED4>",
    "<END_EXTENDED>",
    "<ARBITRARY>",
    "<HEX_ONE>",
    "<HEX_TWO>",
    "<HEX_FOUR>",
  };

}
