from anytree import Node, RenderTree, AsciiStyle, PostOrderIter
import json
import utils

def ascii_printer(root: Node):
    print(RenderTree(root, style=AsciiStyle()).by_attr())
