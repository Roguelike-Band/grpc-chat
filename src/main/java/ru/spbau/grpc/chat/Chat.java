// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: chat.proto

package ru.spbau.grpc.chat;

public final class Chat {
  private Chat() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ru_spbau_grpc_chat_HelloRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ru_spbau_grpc_chat_HelloRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ru_spbau_grpc_chat_HelloReply_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ru_spbau_grpc_chat_HelloReply_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\nchat.proto\022\022ru.spbau.grpc.chat\"\034\n\014Hell" +
      "oRequest\022\014\n\004name\030\001 \001(\t\"\035\n\nHelloReply\022\017\n\007" +
      "message\030\001 \001(\t2W\n\007Greeter\022L\n\010SayHello\022 .r" +
      "u.spbau.grpc.chat.HelloRequest\032\036.ru.spba" +
      "u.grpc.chat.HelloReplyB\002P\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_ru_spbau_grpc_chat_HelloRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_ru_spbau_grpc_chat_HelloRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ru_spbau_grpc_chat_HelloRequest_descriptor,
        new java.lang.String[] { "Name", });
    internal_static_ru_spbau_grpc_chat_HelloReply_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_ru_spbau_grpc_chat_HelloReply_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ru_spbau_grpc_chat_HelloReply_descriptor,
        new java.lang.String[] { "Message", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
