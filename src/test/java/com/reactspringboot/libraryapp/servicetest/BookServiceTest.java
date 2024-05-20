package com.reactspringboot.libraryapp.servicetest;

import com.reactspringboot.libraryapp.dao.BookRepository;
import com.reactspringboot.libraryapp.dao.CheckoutRepository;
import com.reactspringboot.libraryapp.dao.HistoryRepository;
import com.reactspringboot.libraryapp.dao.PaymentRepository;
import com.reactspringboot.libraryapp.entity.Book;
import com.reactspringboot.libraryapp.entity.Checkout;
import com.reactspringboot.libraryapp.entity.History;
import com.reactspringboot.libraryapp.entity.Payment;
import com.reactspringboot.libraryapp.service.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CheckoutRepository checkoutRepository;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private PaymentRepository paymentRepository;

    private Book book;

    private Checkout checkout;

    private Payment payment;

    private History history;

    private String userEmail;

    @BeforeEach
    public void setup() {

        userEmail = "abcd@mail.com";

        book = Book
                .builder()
                .id(1L)
                .title("Python Beginner")
                .author("XYZ")
                .description("Welcome to Python")
                .copies(10)
                .copiesAvailable(8)
                .category("Programming")
                .img("image")
                .build();

        checkout = Checkout
                .builder()
                .Id(1L)
                .userEmail(userEmail)
                .checkoutDate("2024-01-31")
                .returnDate("2024-02-06")
                .bookId(1L)
                .build();

        history = History
                .builder()
                .id(1L)
                .userEmail(userEmail)
                .checkoutDate("2024-01-31")
                .returnDate("2024-02-06")
                .title("Python Beginner")
                .author("XYZ")
                .description("Welcome to Python")
                .img("image")
                .build();

        payment = Payment
                .builder()
                .id(1L)
                .userEmail(userEmail)
                .amount(100)
                .build();
    }

    // Junit test cases
    @Test
    public void givenUserEmailAndBookId_whenFindByUserEmailAndBookId_thenReturnBookObject() throws ParseException {

        // given - precondition or setup
        //      Book Related Data Setting      //
        Book book1 = Book.builder()
                .id(2L)
                .title("Python Intermediate")
                .author("ZZZ")
                .description("Welcome to Python")
                .copies(10)
                .copiesAvailable(8)
                .category("Programming")
                .img("image")
                .build();

        //      Checked Related Data Setting      //
        Checkout checkout1 = Checkout
                .builder()
                .Id(2L)
                .userEmail(userEmail)
                .checkoutDate("2024-01-21")
                .returnDate("2024-01-27")
                .bookId(2L)
                .build();

        //      Book Related Data Saving and Getting From Book Repository      //

        given(bookRepository.save(book))
                .willReturn(book);
        given(bookRepository.saveAll(List.of(book, book1)))
                .willReturn(List.of(book, book1));
        given(bookRepository.findById(book1.getId()))
                .willReturn(Optional.of(book1));

        //      Book Related Data Saving and Getting From Checkout Repository      //
        given(checkoutRepository.save(checkout1))
                .willReturn(checkout1);
//        given(checkoutRepository.saveAll(List.of(checkout, checkout1)))
//                .willReturn(List.of(checkout, checkout1));
        given(checkoutRepository.findByUserEmailAndBookId(checkout.getUserEmail(), checkout.getBookId()))
                .willReturn(checkout);
        given(checkoutRepository.findBooksByUserEmail(userEmail))
                .willReturn(List.of(checkout1));

        given(paymentRepository.save(payment))
                .willReturn(payment);
        given(paymentRepository.findByUserEmail(payment.getUserEmail()))
                .willReturn(null);

        // when - action or the behaviour that we are going test
        Optional<Book> getBook = bookRepository.findById(book.getId());
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(checkout.getUserEmail(), checkout.getBookId());
        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean bookNeedsReturned = false;

        for(Checkout checkout : checkoutList) {
            Date d1 = sdf.parse(checkout.getReturnDate());
            Date d2 = sdf.parse(LocalDate.now().toString());

            TimeUnit time = TimeUnit.DAYS;

            double differenceInTime = time.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);

            if(differenceInTime < 0) {
                bookNeedsReturned = true;
                break;
            }
        }

        Payment getPayment = paymentRepository.findByUserEmail(userEmail);
//        if(getPayment==null) {
            Payment payment1 = new Payment();
            payment1.setId(2L);
            payment1.setAmount(00.00);
            payment1.setUserEmail(userEmail);
            given(paymentRepository.save(payment1)).willReturn(payment1);
            given(paymentRepository.findById(payment1.getId())).willReturn(Optional.of(payment1));
            Payment getPayment1 = paymentRepository.findById(payment1.getId()).get();
//        }




        // then - verify the output
        Assertions.assertThat(getBook.stream().count()).isEqualTo(0);
        Assertions.assertThat(validateCheckout).isEqualTo(checkout);
        Assertions.assertThat(checkoutList.size()).isEqualTo(1);

        Assertions.assertThat(bookNeedsReturned).isEqualTo(true);

//        Assertions.assertThat(getPayment).isEqualTo(payment);
        Assertions.assertThat(getPayment).isNull();

        Assertions.assertThat(getPayment1).isEqualTo(payment1);
    }
}
