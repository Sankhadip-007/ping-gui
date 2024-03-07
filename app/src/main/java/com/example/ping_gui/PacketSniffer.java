package com.example.ping_gui;

import androidx.annotation.NonNull;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.factory.PacketFactories;
import org.pcap4j.packet.factory.PacketFactory;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.NifSelector;
import org.pcap4j.packet.namednumber.NamedNumber;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeoutException;

public class PacketSniffer {

    private PcapHandle handle;
    //private PacketFactory<Packet, NamedNumber<?, ?>> factory;
    private PacketFactory<Packet, EtherType> factory;

    public PacketSniffer() {
        Type type = new ParameterizedType() {
            @NonNull
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{Packet.class, EtherType.class};
            }

            @NonNull
            @Override
            public Type getRawType() {
                return PacketFactory.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        factory = PacketFactories.getFactory(Packet.class, EtherType.class);
    }
   // check either with startsniffin 1 or 2
    public void startSniffing1() {
        try {
            String device = String.valueOf(new NifSelector().selectNetworkInterface());
            if (device != null) {
                handle = new PcapHandle.Builder(device)
                        .snaplen(65536)
                        .promiscuousMode(PcapNetworkInterface.PromiscuousMode.PROMISCUOUS)
                        .timeoutMillis(10)
                        .build();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (true) {
                                Packet packet = handle.getNextPacketEx();
                                if (packet != null) {
                                    // Process the packet here
                                    System.out.println(packet);
                                }
                            }
                        } catch (PcapNativeException | NotOpenException e) {
                            e.printStackTrace();
                        } catch (EOFException e) {
                            throw new RuntimeException(e);
                        } catch (TimeoutException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        } catch (PcapNativeException e) {
            e.printStackTrace();
        } catch (EOFException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void startSniffing2() {
        try {
            String device = String.valueOf(new NifSelector().selectNetworkInterface());
            if (device != null) {
                handle = new PcapHandle.Builder(device)
                        .snaplen(65536)
                        .promiscuousMode(PcapNetworkInterface.PromiscuousMode.PROMISCUOUS)
                        .timeoutMillis(10)
                        .build();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (true) {
                                Packet packet = handle.getNextPacketEx();
                                if (packet != null) {
                                    // Process the packet here
                                    System.out.println(packet);
                                }
                            }
                        } catch (PcapNativeException | NotOpenException e) {
                            e.printStackTrace();
                        } catch (EOFException e) {
                            throw new RuntimeException(e);
                        } catch (TimeoutException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
        } catch (PcapNativeException | IOException e) {
            e.printStackTrace();
        }
    }
    public void stopSniffing() {
        if (handle != null && handle.isOpen()) {
            handle.close();
        }
    }
}
