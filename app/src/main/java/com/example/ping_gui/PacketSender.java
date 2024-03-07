package com.example.ping_gui;
import static android.app.PendingIntent.getActivity;
import static android.content.Context.WIFI_SERVICE;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UnknownPacket;
import org.pcap4j.packet.factory.PacketFactories;
import org.pcap4j.packet.factory.PacketFactory;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.MacAddress;
import org.pcap4j.util.NifSelector;
import org.pcap4j.packet.namednumber.NamedNumber;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeoutException;


public class PacketSender extends AppCompatActivity {
    private PcapHandle handle;
    WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

    WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
    final String MAC_ADDRESS = wifiInfo.getMacAddress();

    public PacketSender() {
        try {
            PcapNetworkInterface nif = Pcaps.getDevByName("wlan0"); // Change to the appropriate network interface
            handle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
        } catch (PcapNativeException e) {
            e.printStackTrace();
        }
    }

    public void sendPacket() {
        try {
            EthernetPacket.Builder etherBuilder = new EthernetPacket.Builder()
                    .srcAddr(MacAddress.getByName(MAC_ADDRESS)) // Change to the source MAC address
                    .dstAddr(MacAddress.getByName("ff:ff:ff:ff:ff:ff")) // Change to the destination MAC address
                    .type(EtherType.IPV4)
                    .payloadBuilder(new UnknownPacket.Builder().rawData(new byte[]{0x00, 0x01, 0x02, 0x03})); // Change to the payload data
            Packet packet = etherBuilder.build();

            handle.sendPacket(packet);
        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }
    public void close() {
        if (handle != null && handle.isOpen()) {
            handle.close();
        }
    }
}
