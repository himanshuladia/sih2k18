from tabula import convert_into
from tqdm import tqdm

for i in tqdm(range(1, 4888)):
    oldFilename = '/home/himanshuladia/Smart India Hackathon 2k18/scraped/pdfs/' + str(i) + '.pdf'
    newFilename = '/home/himanshuladia/Smart India Hackathon 2k18/scraped/csvs/' + str(i) + '.csv'
    convert_into(oldFilename, newFilename, output_format='csv')

# Method 2
# from io import StringIO
# from pdfminer.pdfinterp import PDFResourceManager, PDFPageInterpreter
# from pdfminer.converter import TextConverter
# from pdfminer.layout import LAParams
# from pdfminer.pdfpage import PDFPage
# import os
# import sys
# import getopt

# # converts pdf, returns its text content as a string


# def convert(fname, pages=None):
#     if not pages:
#         pagenums = set()
#     else:
#         pagenums = set(pages)

#     output = StringIO()
#     manager = PDFResourceManager()
#     converter = TextConverter(manager, output, laparams=LAParams())
#     interpreter = PDFPageInterpreter(manager, converter)

#     infile = open(fname, 'rb')
#     for page in PDFPage.get_pages(infile, pagenums):
#         interpreter.process_page(page)
#     infile.close()
#     converter.close()
#     text = output.getvalue()
#     output.close
#     return text


# for i in range(1, 1071):
#     oldfilename = str(i) + '.pdf'
#     newfilename = str(i) + '.txt'
#     x = convert(oldfilename)
#     print(str((i / 1070) * 100) + '%', end=" ")
#     with open(newfilename, 'w') as f:
#         f.write(x)
