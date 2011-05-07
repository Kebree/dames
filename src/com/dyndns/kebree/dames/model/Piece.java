package com.dyndns.kebree.dames.model;

public class Piece {
	public enum Color{
		black("Black"), white("White"), none("None");
		
		protected String label;
		Color(String label){
			this.label = label;
		}
		
		public String getLabel(){
			return label;
		}
	}
	
	public enum PieceType{
		queen("Queen"), pawn("Pawn");
		
		protected String label;
		PieceType(String label){
			this.label = label;
		}
		
		public String getLabel(){
			return label;
		}
	}

	private Color color;
	private PieceType ptype;	

	public Piece(){
		color = Color.none;
		ptype = PieceType.pawn;
	}
	
	public Piece(Color c, PieceType p) {
		color=c;
		ptype = p;
	}
	
	/**
	 * @return the piece type
	 */
	public PieceType getPtype() {
		return ptype;
	}

	/**
	 * @param ptype the piece type to set
	 */
	public void setPtype(PieceType ptype) {
		this.ptype = ptype;
	}
	
	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
}
