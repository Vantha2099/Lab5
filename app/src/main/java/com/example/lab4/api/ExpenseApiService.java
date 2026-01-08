package com.example.lab4.api;

import com.example.lab4.model.Expense;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ExpenseApiService {

    @GET("expenses")
    Call<List<Expense>> getExpenses(
            @Header("X-DB-NAME") String dbName,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @GET("expenses/{id}")
    Call<Expense> getExpenseById(
            @Header("X-DB-NAME") String dbName,
            @Path("id") String expenseId
    );

    @POST("expenses")
    Call<Expense> createExpense(
            @Header("X-DB-NAME") String dbName,
            @Body Expense expense
    );

    @DELETE("expenses/{id}")
    Call<Void> deleteExpense(
            @Header("X-DB-NAME") String dbName,
            @Path("id") String expenseId
    );
}