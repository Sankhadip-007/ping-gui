package com.example.ping_gui;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.factory.PacketFactories;
import org.pcap4j.packet.factory.PacketFactory;
import org.pcap4j.util.NifSelector;
import org.pcap4j.packet.namednumber.NamedNumber;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeoutException;

public class PacketSniffer {

    private PcapHandle handle;
    private PacketFactory<Packet, NamedNumber<?, ?>> factory;

    public PacketSniffer() {
        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{Packet.class, NamedNumber.class};
            }

            @Override
            public Type getRawType() {
                return PacketFactory.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        factory = PacketFactories.getFactory(type);
    }

    public void startSniffing() {
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

    public void stopSniffing() {
        if (handle != null && !handle.isClosed()) {
            handle.close();
        }
    }
}
