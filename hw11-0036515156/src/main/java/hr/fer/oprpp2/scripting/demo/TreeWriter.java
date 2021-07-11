package hr.fer.oprpp2.scripting.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import hr.fer.oprpp2.scripting.nodes.DocumentNode;
import hr.fer.oprpp2.scripting.nodes.EchoNode;
import hr.fer.oprpp2.scripting.nodes.ForLoopNode;
import hr.fer.oprpp2.scripting.nodes.INodeVisitor;
import hr.fer.oprpp2.scripting.nodes.TextNode;
import hr.fer.oprpp2.scripting.parser.SmartScriptParser;

public class TreeWriter {
	
	public static class WriterVisitor implements INodeVisitor {

		private StringBuilder sb;
		
		public WriterVisitor() {
			this.sb = new StringBuilder();
		}
		
		@Override
		public void visitTextNode(TextNode node) {
			sb.append(node.toString());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			sb.append(node.toString());
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			sb.append(node.toString());
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i=0;i<node.numberOfChildren();i++) {
				node.getChild(i).accept(this);
			}
			System.out.println(sb.toString());
		}
	}

	public static void main(String[] args) {
		
		if (args.length!=1) {
			System.out.println("1 arguments expected. Filename.");
			return;
		}
		File file = null;
		try {
			file = new File(args[0]);
		}catch(Exception e) { System.out.println("Couldn't open file for given filename."); }
		
		String docBody = null;
		try {
			docBody = Files.readString(file.toPath());
		} catch (IOException e) {
			System.out.println("Unable to read from file.");
			e.printStackTrace();
		}
		
		SmartScriptParser parser = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		parser.getDocumentNode().accept(visitor);
	}
}
