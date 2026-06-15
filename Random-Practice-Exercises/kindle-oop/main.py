"""
Basic exercise that represents a amazon kindle-like device. It has the total library of books, users that can have active books, and tracks the current page number of each book in a users library.

Classes:
- Total Library: A library of all books currently in the system.
- User: Basic information about the user, such as name, email, and the list of active books they have.
- Book: Title and list of page objects
- Page: Represents a page in a book, such as page number and string content.
"""

# Track each page of a given book
class Page:
    contents: string
    page_num: int
    
    def __init__(self):
        self.contents = ""
        self.page_num = 0

# Track the details of each book
class Book:
    title: string
    pages[]: list(Page)

    def __init__(self):
        self.title = ""
        self.pages = []

# All books in the system
class TotalLibrary:
    books: list 

    def __init__(self):
        self.books = []

    # Exposed via endpoint to publishers
    def add_book(self, book):
        self.books.append(book)

class Library:
    active_books[]: list(Book)
    last_view_date: string

class User:
    name: string
    email: string
    password: string
    libraries[]: list(Library)

class ActuiveBook:
    book: Book
    current_page_num: int
    last_view_date: string

def add_to_total_library(book):
    total_library.add_book(book)

def add_to_user_library(user, book):
    active_book = ActiveBook()
    active_book.book = book
    active_book.current_page_num = 0
    active_book.last_view_date = today()
    user.libraries.append(active_book)

def main():
    user = get_user()
    user_libraries = user.libraries
    for library in user_libraries:
        active_books = library.active_books
        for active_book in active_books:
            print(active_book.book.title)

    cur_book = input_book("Select book title to read")

    print(cur_book.pages[active_book.current_page_num].contents))
