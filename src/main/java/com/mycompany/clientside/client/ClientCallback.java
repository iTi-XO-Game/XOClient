/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.clientside.client;

/**
 *
 * @author Hossam
 */
@FunctionalInterface
public interface ClientCallback {

    public void onSuccess(String responseJson);

    default public void onFailure() {
    }
}
